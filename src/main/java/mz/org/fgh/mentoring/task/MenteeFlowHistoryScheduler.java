package mz.org.fgh.mentoring.task;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.runtime.event.ApplicationStartupEvent;

import io.micronaut.context.event.ApplicationEventListener;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.enums.EnumFlowHistory;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.tutored.FlowHistoryRepository;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.service.tutored.FlowHistoryProgressStatusService;
import mz.org.fgh.mentoring.service.tutored.MenteeFlowHistoryService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.micronaut.http.server.netty.types.stream.NettyStreamedCustomizableResponseType.LOG;

@Singleton
public class MenteeFlowHistoryScheduler implements ApplicationEventListener<ApplicationStartupEvent> {

    private final MenteeFlowHistoryService menteeFlowHistoryService;
    private final FlowHistoryRepository flowHistoryRepository;
    private final FlowHistoryProgressStatusService flowHistoryProgressStatusService;
    private final SettingService settingService;
    private final TutoredService tutoredService;

    private ScheduledExecutorService scheduler;

    public MenteeFlowHistoryScheduler(MenteeFlowHistoryService menteeFlowHistoryService, FlowHistoryRepository flowHistoryRepository, FlowHistoryProgressStatusService flowHistoryProgressStatusService, SettingService settingService,
                                      TutoredService tutoredService) {
        this.menteeFlowHistoryService = menteeFlowHistoryService;
        this.flowHistoryRepository = flowHistoryRepository;
        this.flowHistoryProgressStatusService = flowHistoryProgressStatusService;
        this.settingService = settingService;
        this.tutoredService = tutoredService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartupEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleNextRun();
    }

    private void scheduleNextRun() {
        int intervalMinutes = 5; // padrão
        try {
            Setting setting = settingService.getSettingByDeignation("FLOW_HISTORY_JOB_INTERVAL_MIN")
                .orElseThrow(() -> new MentoringBusinessException("Setting not found for DESIGNATION: FLOW_HISTORY_JOB_INTERVAL_MIN"));
            if (!setting.getEnabled())
                throw new MentoringBusinessException("Setting FLOW_HISTORY_JOB_INTERVAL_MIN is not enabled");
            intervalMinutes = Integer.parseInt(setting.getValue());
        } catch (Exception e) {
            System.err.println("Erro ao obter interval. Usando 5 minutos. " + e.getMessage());
        }

        scheduler.schedule(() -> {
            try {
                processMenteeFlowHistories();
            } catch (Exception e) {
                LOG.error("Erro ao processar MenteeFlowHistory: " + e.getMessage());
            } finally {
                scheduleNextRun();
            }
        }, intervalMinutes, TimeUnit.MINUTES);
    }

    private void processMenteeFlowHistories() {


        int menteeRondaRemovalInterval = 60; // padrão



        try {
            Setting setting = settingService.getSettingByDeignation("Mentee_ronda_removal_interval")
                    .orElseThrow(() -> new MentoringBusinessException("Setting not found for DESIGNATION: Mentee_ronda_removal_interval"));
            if (!setting.getEnabled())
                throw new MentoringBusinessException("Setting Mentee_ronda_removal_interval is not enabled");
            menteeRondaRemovalInterval = Integer.parseInt(setting.getValue());
        } catch (Exception e) {
            System.err.println("Erro ao obter interval. Usando 60 minutos. " + e.getMessage());
        }

        // 🔹 1. Buscar todos estados de forma centralizada e segura
        Map<EnumFlowHistoryProgressStatus, FlowHistoryProgressStatus> statusMap = flowHistoryProgressStatusService.findAllByCodes(
                EnumSet.of(
                        EnumFlowHistoryProgressStatus.INTERROMPIDO,
                        EnumFlowHistoryProgressStatus.AGUARDA_INICIO,
                        EnumFlowHistoryProgressStatus.INICIO
                )
        );

        FlowHistoryProgressStatus estadoInterrompido = statusMap.get(EnumFlowHistoryProgressStatus.INTERROMPIDO);
        FlowHistoryProgressStatus estadoAguardaInicio = statusMap.get(EnumFlowHistoryProgressStatus.AGUARDA_INICIO);
        FlowHistoryProgressStatus estadoInicio = statusMap.get(EnumFlowHistoryProgressStatus.INICIO);

//        FlowHistory sessaoSemestral = flowHistoryRepository.findByName(EnumFlowHistory.SESSAO_SEMESTRAL.getLabel())
//                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND,
//                        EnumFlowHistory.SESSAO_SEMESTRAL.getLabel() + " não encontrada"));

        String estagioRondaCiclo = EnumFlowHistory.RONDA_CICLO.getCode();

        // 🔹 2. Interromper Rondas/Ciclos inicioados e nao terminados em 60 dias
        menteeFlowHistoryService.findRondasOuCicloAtcIniciadasHaMaisDe60Dias(estagioRondaCiclo, estadoInicio.getCode(), menteeRondaRemovalInterval)
            .forEach(history -> {
                Tutored tutored = history.getTutored();

                createAndSaveMenteeFlowHistory(tutored, history.getFlowHistory(), estadoInterrompido, null);
                createAndSaveMenteeFlowHistory(tutored, history.getFlowHistory(), estadoAguardaInicio, null);

                // Remove from ronda
                if (history.getRonda() != null && !history.getRonda().getRondaMentees().isEmpty())
                    menteeFlowHistoryService.removeTutoredFromRonda(history.getRonda().getRondaMentees(), tutored);
            });

        // 🔹 3. Enviar para Sessão Semestral os que completaram Ronda há +6 meses
//        menteeFlowHistoryService.findRondaTerminadaHaMaisDe6Meses()
//        .forEach(history -> {
//            Tutored tutored = history.getTutored();
//            createAndSaveMenteeFlowHistory(tutored, sessaoSemestral, estadoAguardaInicio, null);
//        });
    }

    private void createAndSaveMenteeFlowHistory(
            Tutored tutored,
            FlowHistory flowHistory,
            FlowHistoryProgressStatus progressStatus,
            @Nullable Ronda ronda
    ) {
        MenteeFlowHistory newHistory = new MenteeFlowHistory();
        newHistory.setTutored(tutored);
        newHistory.setFlowHistory(flowHistory);
        newHistory.setProgressStatus(progressStatus);
        newHistory.setRonda(ronda);
        newHistory.setClassification(0.0); // Valor padrão

        menteeFlowHistoryService.saveFromSchedule(newHistory);
    }

    private void interruptMenteeFlowHistory(
            MenteeFlowHistory toUpdate,
            FlowHistoryProgressStatus newProgressStatus
    ) {
        toUpdate.setProgressStatus(newProgressStatus);

        menteeFlowHistoryService.interruptionFromSchedule(toUpdate);
    }




    @PreDestroy
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
