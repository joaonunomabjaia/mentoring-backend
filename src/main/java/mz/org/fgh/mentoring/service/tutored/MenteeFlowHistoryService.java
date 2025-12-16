package mz.org.fgh.mentoring.service.tutored;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutored.MenteeFlowHistoryDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.ronda.RondaMenteeRepository;
import mz.org.fgh.mentoring.repository.tutored.MenteeFlowHistoryRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.util.*;

@Singleton
public class MenteeFlowHistoryService {

    private final MenteeFlowHistoryRepository menteeFlowHistoryRepository;
    private final RondaMenteeRepository rondaMenteeRepository;
    private final TutoredRepository tutoredRepository;

    public MenteeFlowHistoryService(MenteeFlowHistoryRepository menteeFlowHistoryRepository, RondaMenteeRepository rondaMenteeRepository, TutoredRepository tutoredRepository) {
        this.menteeFlowHistoryRepository = menteeFlowHistoryRepository;
        this.rondaMenteeRepository =  rondaMenteeRepository;
        this.tutoredRepository = tutoredRepository;
    }

    // 🔍 Buscar por ID
    public Optional<MenteeFlowHistory> findById(Long id) {
        return menteeFlowHistoryRepository.findById(id);
    }

    // 🔍 Buscar último histórico de um mentorando
    public MenteeFlowHistory findLastByMentee(Tutored mentee) {
        return menteeFlowHistoryRepository.findTopByTutoredOrderByCreatedAtDesc(mentee).orElse(null);
    }

    // 🔍 Buscar FlowHistory pelo código
    public FlowHistory findFlowHistoryByName(String name) {
        return menteeFlowHistoryRepository.findFlowHistoryByCode(name)
                .orElseThrow(() -> new RuntimeException("FlowHistory não encontrado: " + name))
                .getFlowHistory();
    }

    // 💾 Criar novo histórico
    /*@Transactional
    public MenteeFlowHistory save(MenteeFlowHistory history, User user) {

        history.setCreatedBy(user.getUuid());
        history.setUuid(Utilities.generateUUID());
        history.setCreatedAt(DateUtils.getCurrentDate());
        history.setLifeCycleStatus(LifeCycleStatus.ACTIVE);


        // 🔢 Calcula o número sequencial por Tutored
        if (history.getSequenceNumber() == null && history.getTutored() != null) {
            int nextSeq = menteeFlowHistoryRepository.countByTutored(history.getTutored()) + 1;
            history.setSequenceNumber(nextSeq);
        }

        return menteeFlowHistoryRepository.save(history);
    }*/

    @Transactional
    public void saveFromSchedule(MenteeFlowHistory history) {

        history.setCreatedBy("System");
        history.setUuid(Utilities.generateUUID());
        history.setCreatedAt(DateUtils.getCurrentDate());
        history.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        // 🔢 Calcula o número sequencial por Tutored
        if (history.getSequenceNumber() == null && history.getTutored() != null) {
            int nextSeq = menteeFlowHistoryRepository.countByTutored(history.getTutored()) + 1;
            history.setSequenceNumber(nextSeq);
        }

        menteeFlowHistoryRepository.save(history);
    }

    @Transactional
    public void interruptionFromSchedule(MenteeFlowHistory history) {

        history.setUpdatedBy("System");
        history.setUpdatedAt(DateUtils.getCurrentDate());

        menteeFlowHistoryRepository.update(history);
    }

    // 🔄 Atualizar histórico existente
    @Transactional
    public MenteeFlowHistory update(MenteeFlowHistory history) {
        MenteeFlowHistory existing = menteeFlowHistoryRepository.findByUuid(history.getUuid())
                .orElseThrow(() -> new RuntimeException("MenteeFlowHistory não encontrado com UUID: " + history.getUuid()));

        existing.setProgressStatus(history.getProgressStatus());
        existing.setFlowHistory(history.getFlowHistory());
        //existing.setTutored(history.getTutored());
        //existing.setRonda(history.getRonda());
        existing.setUpdatedAt(DateUtils.getCurrentDate());
        existing.setUpdatedBy(history.getUpdatedBy());
        existing.setLifeCycleStatus(history.getLifeCycleStatus());

        return menteeFlowHistoryRepository.update(existing);
    }

    /**
     * Filtra MenteeFlowHistory pelo flowHistory.code e progressStatus.code desejados.
     * Traz apenas aqueles cujo tutored não tem nenhuma Mentorship realizada nos últimos 60 dias.
     * Esses serao interrompidos
     */
    public List<MenteeFlowHistory> findRondasOuCicloAtcIniciadasHaMaisDe60Dias(String flowHistoryCode, String flowHistoryStatusCode, int menteeRondaRemovalInterval){
        return menteeFlowHistoryRepository.findRondasOuCicloAtcIniciadasSemMentorshipHaMaisDe60Dias(flowHistoryCode, flowHistoryStatusCode, menteeRondaRemovalInterval);
    }

    /**
     * Retorna todos os MenteeFlowHistory com FlowHistory "SESSAO_SEMESTRAL"
     * e ProgressStatus "AGUARDA INICIO", criados há 3 meses ou mais.
     * Esses vao para de novo a sessao semastral
     */
    public List<MenteeFlowHistory> findRondaTerminadaHaMaisDe6Meses(){
        return menteeFlowHistoryRepository.findRondaTerminadaHaMaisDe6Meses();
    }

    // 🔎 Buscar com filtros + paginação
    public Page<MenteeFlowHistoryDTO> findFiltered(
            @Nullable String menteeName,
            @Nullable String progressStatus,
            @Nullable String flowHistoryName,
            @Nullable Date startDate,
            @Nullable Date endDate,
            Pageable pageable
    ) {
        Page<MenteeFlowHistory> page = menteeFlowHistoryRepository.findFiltered(
                menteeName,
                progressStatus,
                flowHistoryName,
                startDate,
                endDate,
                pageable
        );

        // ✅ Evita NPE e limpa a lista interna da ronda
        page.getContent().forEach(mfh -> {
            if (mfh.getRonda() != null) {
                mfh.getRonda().setRondaMentees(new HashSet<>());
                mfh.getRonda().setRondaMentors(new HashSet<>());
            }
        });

        return page.map(MenteeFlowHistoryDTO::new);
    }

    public void deleteByRonda(Ronda ronda) {
        menteeFlowHistoryRepository.deleteByRonda(ronda);
    }

    @Transactional
    public void removeTutoredFromRonda(Set<RondaMentee> rondaMentees, Tutored tutored) {
        rondaMentees.forEach( rondaMentee ->  {
            if (rondaMentee.getTutored().equals(tutored)) {
                rondaMentee.setEndDate(DateUtils.getCurrentDate());
                rondaMentee.setUpdatedAt(DateUtils.getCurrentDate());
                rondaMentee.setUpdatedBy("System");

                rondaMenteeRepository.update(rondaMentee);
            }
        });
    }

    @Transactional
    public MenteeFlowHistory save(MenteeFlowHistory menteeFlowHistory, User user) {
        // assume que já tens algo semelhante; só para lembrar o audit
        if (menteeFlowHistory.getId() == null) {
            menteeFlowHistory.setCreatedAt(DateUtils.getCurrentDate());
            menteeFlowHistory.setCreatedBy(user.getUuid());
            menteeFlowHistory.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        } else {
            menteeFlowHistory.setUpdatedAt(DateUtils.getCurrentDate());
            menteeFlowHistory.setUpdatedBy(user.getUuid());
        }
        return menteeFlowHistoryRepository.save(menteeFlowHistory);
    }

    public void deleteByTutored(Tutored tutored) {
        menteeFlowHistoryRepository.deleteByTutored(tutored);
    }

    public List<MenteeFlowHistory> findByFlowAndStatus(String flowCode, String statusCode) {
        return menteeFlowHistoryRepository.findActiveByFlowAndStatus(flowCode, statusCode, LifeCycleStatus.ACTIVE);
    }

    public List<MenteeFlowHistory> findAllByFlowAndStatus(String flowCode, String statusCode) {
        return menteeFlowHistoryRepository
                .findAllByFlowHistoryCodeAndProgressStatusCode(flowCode, statusCode);
    }

    public List<MenteeFlowHistory> findByRonda(Ronda ronda) {
        return menteeFlowHistoryRepository.findByRonda(ronda);
    }

    public List<MenteeFlowHistory> findByTutored(Tutored tutored) {
        return menteeFlowHistoryRepository.findByTutored(tutored);
    }

    /**
     * Garante que o Tutored tem ID carregado da BD.
     * - Se já tiver ID, retorna o mesmo objeto;
     * - Se tiver apenas UUID, recarrega da BD;
     * - Se não conseguir recarregar, retorna null ou lança exceção (aqui lancei).
     */
    public Tutored reloadTutoredIfNeeded(Tutored tutored) {
        if (tutored == null) {
            return null;
        }

        if (tutored.getId() != null) {
            return tutored;
        }

        if (!Utilities.stringHasValue(tutored.getUuid())) {
            throw new RuntimeException("Tutored sem ID e sem UUID válido para reload");
        }

        Optional<Tutored> opt = tutoredRepository.findByUuid(tutored.getUuid());
        return opt.orElseThrow(() ->
                new RuntimeException("Tutored não encontrado para UUID: " + tutored.getUuid()));
    }

    public void inactivatePreviousHistories(Tutored tutored) {
        menteeFlowHistoryRepository.inactivatePreviousHistories(tutored.getId(), LifeCycleStatus.INACTIVE);
    }
}
