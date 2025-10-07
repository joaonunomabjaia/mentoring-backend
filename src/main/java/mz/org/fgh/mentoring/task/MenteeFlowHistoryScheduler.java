package mz.org.fgh.mentoring.task;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.runtime.event.ApplicationStartupEvent;

import io.micronaut.context.event.ApplicationEventListener;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.enums.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.enums.FlowHistoryStatus;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
import mz.org.fgh.mentoring.repository.tutored.FlowHistoryRepository;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.service.tutored.MenteeFlowHistoryService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.micronaut.http.server.netty.types.stream.NettyStreamedCustomizableResponseType.LOG;

@Singleton
public class MenteeFlowHistoryScheduler implements ApplicationEventListener<ApplicationStartupEvent> {

    private final MenteeFlowHistoryService menteeFlowHistoryService;
    private final FlowHistoryRepository flowHistoryRepository;
    private final SettingService settingService;
    private final TutoredService tutoredService;

    private ScheduledExecutorService scheduler;

    public MenteeFlowHistoryScheduler(MenteeFlowHistoryService menteeFlowHistoryService, FlowHistoryRepository flowHistoryRepository, SettingService settingService,
                                      TutoredService tutoredService) {
        this.menteeFlowHistoryService = menteeFlowHistoryService;
        this.flowHistoryRepository = flowHistoryRepository;
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
        List<MenteeFlowHistory> completedHistories = menteeFlowHistoryService.findCompletedRondaMentoria();

        FlowHistory flowHistory = flowHistoryRepository.findByName(FlowHistoryStatus.SESSAO_SEMESTRAL.name())
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "SESSAO_SEMESTRAL não encontrada"));

        for (MenteeFlowHistory history : completedHistories) {
            Tutored tutored = history.getTutored();

            MenteeFlowHistory newHistory = new MenteeFlowHistory();
            newHistory.setTutored(tutored);
            newHistory.setFlowHistory(flowHistory);
            newHistory.setProgressStatus(FlowHistoryProgressStatus.ELEGIVEL);
            newHistory.setUuid(Utilities.generateUUID());
            newHistory.setCreatedBy("System");
            newHistory.setCreatedAt(DateUtils.getCurrentDate());
            newHistory.setRonda(null); // opcional

            menteeFlowHistoryService.save(newHistory);
            LOG.info("Criada nova MenteeFlowHistory para tutored: " + tutored.getUuid());
        }
    }


    @PreDestroy
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
