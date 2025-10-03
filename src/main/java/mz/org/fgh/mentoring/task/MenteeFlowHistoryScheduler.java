package mz.org.fgh.mentoring.task;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.runtime.event.ApplicationStartupEvent;

import io.micronaut.context.event.ApplicationEventListener;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.enums.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.repository.tutored.FlowHistoryRepository;
import mz.org.fgh.mentoring.service.tutored.MenteeFlowHistoryService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class MenteeFlowHistoryScheduler implements ApplicationEventListener<ApplicationStartupEvent> {

    private final MenteeFlowHistoryService menteeFlowHistoryService;
    private final FlowHistoryRepository flowHistoryRepository;
    private final TutoredService tutoredService;

    private ScheduledExecutorService scheduler;

    public MenteeFlowHistoryScheduler(MenteeFlowHistoryService menteeFlowHistoryService, FlowHistoryRepository flowHistoryRepository,
                                      TutoredService tutoredService) {
        this.menteeFlowHistoryService = menteeFlowHistoryService;
        this.flowHistoryRepository = flowHistoryRepository;
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
            // Aqui podemos buscar settings se quisermos que seja dinamico
        } catch (Exception e) {
            System.err.println("Erro ao obter interval. Usando 5 minutos. " + e.getMessage());
        }

        scheduler.schedule(() -> {
            try {
                processMenteeFlowHistories();
            } catch (Exception e) {
                System.err.println("Erro ao processar MenteeFlowHistory: " + e.getMessage());
            } finally {
                scheduleNextRun();
            }
        }, intervalMinutes, TimeUnit.MINUTES);
    }

    private void processMenteeFlowHistories() {
        List<MenteeFlowHistory> completedHistories = menteeFlowHistoryService.findCompletedRondaMentoria();

        FlowHistory flowHistory = flowHistoryRepository.findByName("SESSAO_SEMESTRAL")
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, "SESSAO_SEMESTRAL não encontrado"));

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
            System.out.println("Criada nova MenteeFlowHistory para tutored: " + tutored.getUuid());
        }
    }


    @PreDestroy
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
