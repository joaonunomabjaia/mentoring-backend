package mz.org.fgh.mentoring.task;


import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.service.session.SessionService;
import mz.org.fgh.mentoring.service.setting.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Singleton
public class SessionSummaryScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(SessionSummaryScheduler.class);

    private final SessionService sessionService;
    private final SettingService settingService;

    public SessionSummaryScheduler(SessionService sessionService, SettingService settingService) {
        this.sessionService = sessionService;
        this.settingService = settingService;
    }

    @Scheduled(fixedDelay = "10m")
    void generateMissingSummaries() {
        Optional<Setting> optionalSetting = settingService.getSettingByDeignation(Setting.AI_SESSION_SUMMARY_GENERATION);

        if (optionalSetting.isPresent() && optionalSetting.get().getEnabled()) {
            sessionService.generateSummary();
        }
        else {
            LOG.info("AI Session summary generation is disabled.");
        }
    }
}