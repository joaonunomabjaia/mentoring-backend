package mz.org.fgh.mentoring.service.tutored;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutored.MenteeFlowHistoryDTO;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.tutored.MenteeFlowHistoryRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Singleton
public class MenteeFlowHistoryService {

    private final MenteeFlowHistoryRepository menteeFlowHistoryRepository;

    public MenteeFlowHistoryService(MenteeFlowHistoryRepository menteeFlowHistoryRepository) {
        this.menteeFlowHistoryRepository = menteeFlowHistoryRepository;
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
    @Transactional
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
    }

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

    // 🔄 Atualizar histórico existente
    @Transactional
    public MenteeFlowHistory update(MenteeFlowHistory history) {
        MenteeFlowHistory existing = menteeFlowHistoryRepository.findByUuid(history.getUuid())
                .orElseThrow(() -> new RuntimeException("MenteeFlowHistory não encontrado com UUID: " + history.getUuid()));

        existing.setProgressStatus(history.getProgressStatus());
        existing.setFlowHistory(history.getFlowHistory());
        existing.setTutored(history.getTutored());
        existing.setRonda(history.getRonda());
        existing.setUpdatedAt(DateUtils.getCurrentDate());
        existing.setUpdatedBy(history.getUpdatedBy());

        return menteeFlowHistoryRepository.update(existing);
    }

    /**
     * Retorna todos os MenteeFlowHistory com FlowHistory "RONDA / CICLO ATC"
     * e ProgressStatus "INICIO", criados há 60 ou mais dias.
     * Esses serao interrompidos
     */
    public List<MenteeFlowHistory> findRondasOuCicloAtcIniciadasHaMaisDe60Dias(){
        return menteeFlowHistoryRepository.findRondasOuCicloAtcIniciadasHaMaisDe60Dias();
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



//    public List<MenteeFlowHistory> findCompletedRondaMentoria() {
//        return menteeFlowHistoryRepository.findByFlowHistoryNameAndProgressStatus(
//                EnumFlowHistory.RONDA_MENTORIA.name(),
//                EumFlowHistoryProgressStatus.TERMINADO.name()
//        );
//    }
}
