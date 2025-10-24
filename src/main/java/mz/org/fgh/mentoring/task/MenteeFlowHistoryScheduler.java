package mz.org.fgh.mentoring.task;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
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
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.EnumSet;
import java.util.List;
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

        // 🔹 1. Buscar todos estados e estdos de forma centralizada e segura
        Map<EnumFlowHistoryProgressStatus, FlowHistoryProgressStatus> statusMap = flowHistoryProgressStatusService.findAllByNames(
                EnumSet.of(
                        EnumFlowHistoryProgressStatus.INTERROMPIDO,
                        EnumFlowHistoryProgressStatus.AGUARDA_INICIO,
                        EnumFlowHistoryProgressStatus.INICIO
                )
        );

        FlowHistoryProgressStatus estadoInterrompido = statusMap.get(EnumFlowHistoryProgressStatus.INTERROMPIDO);
        FlowHistoryProgressStatus estadoAguardaInicio = statusMap.get(EnumFlowHistoryProgressStatus.AGUARDA_INICIO);

        FlowHistory sessaoSemestral = flowHistoryRepository.findByName(EnumFlowHistory.SESSAO_SEMESTRAL.name())
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND,
                        EnumFlowHistory.SESSAO_SEMESTRAL.name() + " não encontrada"));

        // 🔹 2. Interromper Rondas/Ciclos que ultrapassaram 60 dias
        menteeFlowHistoryService.findRondasOuCicloAtcIniciadasHaMaisDe60Dias()
                .forEach(history -> {
                    Tutored tutored = history.getTutored();

                    createAndSaveMenteeFlowHistory(tutored, history.getFlowHistory(), estadoInterrompido, history.getRonda());
                    createAndSaveMenteeFlowHistory(tutored, history.getFlowHistory(), estadoAguardaInicio, null);
                });

        // 🔹 3. Enviar para Sessão Semestral os que completaram Ronda há +6 meses
        menteeFlowHistoryService.findRondaTerminadaHaMaisDe6Meses()
                .forEach(history -> {
                    Tutored tutored = history.getTutored();
                    createAndSaveMenteeFlowHistory(tutored, sessaoSemestral, estadoAguardaInicio, null);
                });
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




    @PreDestroy
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
