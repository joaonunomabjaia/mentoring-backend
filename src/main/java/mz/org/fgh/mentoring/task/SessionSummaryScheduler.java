package mz.org.fgh.mentoring.task;


import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.service.session.SessionService;
import mz.org.fgh.mentoring.service.setting.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static mz.org.fgh.mentoring.config.SettingKeys.AI_SESSION_SUMMARY_ENABLED;

@Singleton
public class SessionSummaryScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(SessionSummaryScheduler.class);

    private final SessionService sessionService;
    private final SettingService settings;

    public SessionSummaryScheduler(SessionService sessionService, SettingService settingService) {
        this.sessionService = sessionService;
        this.settings = settingService;
    }

    @Scheduled(cron = "0 0 3 * * *") // Executa diariamente às 03:00
    void generateMissingSummaries() {
        boolean AIGeneration = settings.getBoolean(AI_SESSION_SUMMARY_ENABLED, false);

        if (AIGeneration) {
            sessionService.generateSummary();
        }
        else {
            LOG.info("AI Session summary generation is disabled.");
        }
    }
}