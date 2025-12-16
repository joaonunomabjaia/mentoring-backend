package mz.org.fgh.mentoring.service.tutored;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.enums.EnumFlowHistory;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaMenteeRepository;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static mz.org.fgh.mentoring.config.SettingKeys.RONDA_MENTEE_REMOVAL_INACTIVE_DAYS;
import static mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus.AGUARDA_INICIO;
import static mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus.ISENTO;
import static mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus.INTERROMPIDO;
import static mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus.TERMINADO;

@Singleton
public class MenteeFlowEngineService {

    private final FlowHistoryService flowHistoryService;
    private final FlowHistoryProgressStatusService progressStatusService;
    private final MenteeFlowHistoryService menteeFlowHistoryService;
    private final MentorshipRepository mentorshipRepository;
    private final SettingService settings;
    private final RondaMenteeRepository rondaMenteeRepository;

    @Inject
    public MenteeFlowEngineService(FlowHistoryService flowHistoryService,
                                   FlowHistoryProgressStatusService progressStatusService,
                                   MenteeFlowHistoryService menteeFlowHistoryService,
                                   MentorshipRepository mentorshipRepository,
                                   SettingService settings,
                                   RondaMenteeRepository rondaMenteeRepository) {
        this.flowHistoryService = flowHistoryService;
        this.progressStatusService = progressStatusService;
        this.menteeFlowHistoryService = menteeFlowHistoryService;
        this.mentorshipRepository = mentorshipRepository;
        this.settings = settings;
        this.rondaMenteeRepository = rondaMenteeRepository;
    }


    // ---------- Helpers ----------

    private FlowHistory flow(EnumFlowHistory e) {
        return flowHistoryService.findByCode(e.getCode())
                .orElseThrow(() -> new IllegalStateException("FlowHistory not found: " + e.getCode()));
    }

    private FlowHistoryProgressStatus status(EnumFlowHistoryProgressStatus s) {
        return progressStatusService.findByCode(s.getCode())
                .orElseThrow(() -> new IllegalStateException("FlowHistoryProgressStatus not found: " + s.getCode()));
    }

    private int nextSeq(Tutored tutored) {
        Optional<MenteeFlowHistory> last = tutored.getLastMenteeFlowHistory();
        return last.map(m -> m.getSequenceNumber() == null ? 1 : m.getSequenceNumber() + 1)
                .orElse(1);
    }

    private MenteeFlowHistory createAndSave(Tutored tutored,
                                            FlowHistory fh,
                                            FlowHistoryProgressStatus ps,
                                            Double classification,
                                            Ronda ronda,
                                            User user,
                                            Integer seqOverride) {

        // 1️⃣ Desativar o último histórico ativo (se existir)
        tutored.setMenteeFlowHistories(menteeFlowHistoryService.findByTutored(tutored));
        menteeFlowHistoryService.inactivatePreviousHistories(tutored);

        // 2️⃣ Criar o novo histórico como ACTIVE
        MenteeFlowHistory mfh = new MenteeFlowHistory();
        mfh.setTutored(tutored);
        mfh.setFlowHistory(fh);
        mfh.setProgressStatus(ps);
        mfh.setRonda(ronda);
        mfh.setClassification(classification != null ? classification : 0.0);
        mfh.setSequenceNumber(seqOverride != null ? seqOverride : nextSeq(tutored));
        mfh.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        // campos audit no BaseEntity são setados no service de MenteeFlowHistory
        return menteeFlowHistoryService.save(mfh, user);
    }


    // ---------- 1. Inicialização na criação do Mentee ----------

    /**
     * Chamado quando o mentee é criado no backend.
     *
     * @param tutored  novo mentee (já persistido)
     * @param isIsento true se foi marcado ISENTO à sessão zero no app
     */
    @Transactional
    public void initializeFlowOnCreate(Tutored tutored,
                                       boolean isIsento,
                                       User user) {

        if (isIsento) {
            // 1) SESSAO_ZERO / ISENTO
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.SESSAO_ZERO),
                    status(ISENTO),
                    null,
                    null,
                    user,
                    1
            );

            // 2) RONDA_CICLO / AGUARDA_INICIO
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.RONDA_CICLO),
                    status(AGUARDA_INICIO),
                    null,
                    null,
                    user,
                    2
            );

        } else {
            // NÃO ISENTO: começa em SESSAO_ZERO / AGUARDA_INICIO
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.SESSAO_ZERO),
                    status(AGUARDA_INICIO),
                    null,
                    null,
                    user,
                    1
            );
        }
    }

    // ---------- 3. Ronda criada / iniciada ----------

    /**
     * Chamado quando uma nova Ronda é criada e o mentee é associado
     * (estado ROUND_WAIT -> ROUND_INICIO).
     */
    @Transactional
    public void onRoundStarted(Tutored tutored,
                               Ronda ronda,
                               User user) {

        createAndSave(
                tutored,
                flow(EnumFlowHistory.RONDA_CICLO),
                status(EnumFlowHistoryProgressStatus.INICIO),
                null,
                ronda,
                user,
                null
        );
    }

    // ---------- 4. Ronda concluída ----------

    /**
     * Quando a ronda é concluída: grava TERMINADO e decide o próximo estado.
     *
     * Regra:
     * - score >= 86 → depois de 6 meses envia para SESSAO_SEMESTRAL / AGUARDA_INICIO
     * - score < 86  → volta para RONDA_CICLO / AGUARDA_INICIO
     */
    @Transactional
    public void onRoundFinished(Tutored tutored,
                                Ronda ronda,
                                double classification,
                                User user) {

        if (ronda != null && ronda.isRondaZero()) {
            // 🔹 Caso seja uma Ronda Zero:
            // 1) SESSAO_ZERO / TERMINADO com a classificação alcançada na ronda
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.SESSAO_ZERO),
                    status(TERMINADO),
                    classification,
                    ronda,
                    user,
                    null
            );

            // 2) Cria RONDA_CICLO / AGUARDA_INICIO (sem ronda associada)
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.RONDA_CICLO),
                    status(AGUARDA_INICIO),
                    null,      // classificação inicial 0 / null
                    null,      // sem ronda – vai aguardar uma ronda de ciclo
                    user,
                    null
            );

        } else {
            // 🔹 Ronda de Ciclo "normal":
            // 1) RONDA_CICLO / TERMINADO com a classificação da ronda
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.RONDA_CICLO),
                    status(TERMINADO),
                    classification,
                    ronda,
                    user,
                    null
            );

            // 2) Decide o próximo estado
            if (classification < 86.0) {
                // volta para RONDA_CICLO / AGUARDA_INICIO (sem ronda associada)
                createAndSave(
                        tutored,
                        flow(EnumFlowHistory.RONDA_CICLO),
                        status(AGUARDA_INICIO),
                        null,
                        null,
                        user,
                        null
                );
            } else {
                // >= 86 → pronto para lógica de sessão semestral (+6 meses)
                // O agendamento fica a cargo do scheduler que chamará onSemestralDue().
            }
        }
    }


    // ---------- 5. Sessão semestral devida (após +6 meses) ----------

    /**
     * Chamado por um scheduler que verifica quem tem direito a sessão semestral.
     */
    @Transactional
    public void onSemestralDue(Tutored tutored, User user) {
        createAndSave(
                tutored,
                flow(EnumFlowHistory.SESSAO_SEMESTRAL),
                status(AGUARDA_INICIO),
                null,
                null,
                user,
                null
        );
    }

    /**
     * Quando a sessão semestral é concluída.
     */
    @Transactional
    public void onSemestralFinished(Tutored tutored,
                                    double classification,
                                    User user) {

        // 1) SESSAO_SEMESTRAL / TERMINADO
        createAndSave(
                tutored,
                flow(EnumFlowHistory.SESSAO_SEMESTRAL),
                status(TERMINADO),
                classification,
                null,
                user,
                null
        );

        if (classification >= 86.0) {
            // agenda nova semestral em +6 meses (via scheduler/fila)
        } else {
            // volta para ronda aguarda início
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.RONDA_CICLO),
                    status(AGUARDA_INICIO),
                    null,
                    null,
                    user,
                    null
            );
        }
    }

    // ---------- 6. Interrupção da ronda (60 dias sem sessão) ----------

    /**
     * Marca ronda como INTERROMPIDA e volta para RONDA_CICLO / AGUARDA_INICIO.
     * Chamado pelo scheduler que detecta 60 dias sem sessão.
     */
    @Transactional
    public void onRoundInterrupted(Tutored tutored,
                                   Ronda ronda,
                                   User user) {

        // 0️⃣ Fechar o vínculo RondaMentee (setar endDate)
        rondaMenteeRepository.findByRondaAndTutored(ronda, tutored)
                .ifPresent(rm -> {
                    rm.setEndDate(DateUtils.getCurrentDate());
                    rondaMenteeRepository.update(rm);
                });

        // 1️⃣ RONDA_CICLO / INTERROMPIDO
        createAndSave(
                tutored,
                flow(EnumFlowHistory.RONDA_CICLO),
                status(INTERROMPIDO),
                null,
                ronda,
                user,
                null
        );

        // 2️⃣ Volta para aguardando nova ronda
        createAndSave(
                tutored,
                flow(EnumFlowHistory.RONDA_CICLO),
                status(AGUARDA_INICIO),
                null,
                null,
                user,
                null
        );
    }


    // ---------- 7. Utilitário para scheduler de 60 dias ----------

    /**
     * Verifica MenteeFlowHistory em RONDA_CICLO / INICIO.
     * Para cada par (ronda, mentee), se a última mentoria foi há > 60 dias,
     * marca RONDA_CICLO / INTERROMPIDO e volta para RONDA_CICLO / AGUARDA_INICIO.
     */
    @Transactional
    public void checkAndInterruptInactiveRounds(User systemUser) {
        List<MenteeFlowHistory> ativos = menteeFlowHistoryService
                .findByFlowAndStatus(
                        EnumFlowHistory.RONDA_CICLO.getCode(),
                        EnumFlowHistoryProgressStatus.INICIO.getCode()
                );

        if (ativos.isEmpty()) return;

        Date hoje = DateUtils.getCurrentDate();

        for (MenteeFlowHistory mfh : ativos) {
            if (mfh.getRonda() == null || mfh.getTutored() == null) {
                continue; // dados incompletos
            }

            Long rondaId = mfh.getRonda().getId();
            Long tutoredId = mfh.getTutored().getId();

            // 1️⃣ Última mentoria deste mentee nessa ronda
            Date referencia = mentorshipRepository
                    .findLastPerformedDateByRondaAndTutored(
                            rondaId,
                            tutoredId,
                            LifeCycleStatus.ACTIVE
                    )
                    // se não tiver mentoria, conta desde o início da ronda
                    .orElse(mfh.getRonda().getStartDate());

            if (referencia == null) continue;

            long diffDias = DateUtils.diffInDays(referencia, hoje);

            int maxDays = settings.getInt(RONDA_MENTEE_REMOVAL_INACTIVE_DAYS, 60);
            if (diffDias > maxDays) {
                onRoundInterrupted(mfh.getTutored(), mfh.getRonda(), systemUser);
            }
        }
    }

    // ---------- 8. Quando uma Ronda é apagada ----------

    /**
     * Chamado quando uma Ronda é removida do sistema.
     * Remove todos os históricos de mentees associados à ronda.
     */
    @Transactional
    public void onRondaDeleted(Ronda ronda, User user) {

        // 1️⃣ Buscar todos os históricos associados à ronda (antes de apagar)
        List<MenteeFlowHistory> historiesOfRonda =
                menteeFlowHistoryService.findByRonda(ronda);

        if (historiesOfRonda == null || historiesOfRonda.isEmpty()) {
            // Nada para fazer
            menteeFlowHistoryService.deleteByRonda(ronda);
            return;
        }

        // 2️⃣ Coletar mentees afetados
        Set<Tutored> affectedMentees = historiesOfRonda.stream()
                .map(MenteeFlowHistory::getTutored)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 3️⃣ Apagar todos os históricos dessa ronda
        menteeFlowHistoryService.deleteByRonda(ronda);

        // 4️⃣ Para cada mentee afetado, ativar o histórico com maior sequência
        for (Tutored mentee : affectedMentees) {

            // Garante que temos o mentee “vivo” da BD, se precisares do ID
            if (mentee.getId() == null) {
                mentee = menteeFlowHistoryService
                        .reloadTutoredIfNeeded(mentee); // ou tutoredRepository.findByUuid(...).get()
            }

            List<MenteeFlowHistory> remaining =
                    menteeFlowHistoryService.findByTutored(mentee);

            if (remaining == null || remaining.isEmpty()) {
                // esse mentee ficou sem histórico – nada para ativar
                continue;
            }

            // Escolher o histórico com maior sequenceNumber
            MenteeFlowHistory latest = remaining.stream()
                    .filter(h -> h.getSequenceNumber() != null)
                    .max(Comparator.comparing(MenteeFlowHistory::getSequenceNumber))
                    // fallback: se ninguém tiver sequenceNumber, usa o maior ID
                    .orElseGet(() -> remaining.stream()
                            .max(Comparator.comparing(MenteeFlowHistory::getId))
                            .orElse(null));

            if (latest == null) continue;

            // 5️⃣ Garante que apenas o último fica ACTIVE
            for (MenteeFlowHistory h : remaining) {
                if (h.getId().equals(latest.getId())) {
                    h.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                } else {
                    h.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
                }
                menteeFlowHistoryService.save(h, user);
            }
        }
    }

    // ---------- 9. Mentee removido da Ronda antes de terminar ----------

    /**
     * Chamado quando um mentee é removido de uma Ronda em edição.
     *
     * Requisito: "para os removidos deve ser removido o inicio e voltar para aguarda inicio".
     *
     * Implementação:
     *  - não apagamos o histórico anterior, apenas o marcamos como INACTIVE
     *    (isso já é feito em createAndSave);
     *  - criamos um novo histórico RONDA_CICLO / AGUARDA_INICIO sem ronda associada.
     */
    @Transactional
    public void onRoundMenteeRemoved(Tutored tutored,
                                     Ronda ronda,
                                     User user) {

        // Ronda Zero → voltar para SESSAO_ZERO / AGUARDA_INICIO
        if (ronda != null && ronda.isRondaZero()) {
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.SESSAO_ZERO),
                    status(AGUARDA_INICIO),
                    null,   // classificação 0
                    null,   // sem ronda associada
                    user,
                    null
            );
        } else {
            // Ronda de ciclo → voltar para RONDA_CICLO / AGUARDA_INICIO
            createAndSave(
                    tutored,
                    flow(EnumFlowHistory.RONDA_CICLO),
                    status(AGUARDA_INICIO),
                    null,
                    null,
                    user,
                    null
            );
        }
    }

}
