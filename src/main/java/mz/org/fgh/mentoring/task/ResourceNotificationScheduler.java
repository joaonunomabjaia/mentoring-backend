package mz.org.fgh.mentoring.task;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.service.session.SessionRecommendedResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;

@Singleton
public class ResourceNotificationScheduler {

    public static final Logger LOG = LoggerFactory.getLogger(ResourceNotificationScheduler.class);
    private final SessionRecommendedResourceService sessionRecommendedResourceService;

    public ResourceNotificationScheduler(SessionRecommendedResourceService sessionRecommendedResourceService) {
        this.sessionRecommendedResourceService = sessionRecommendedResourceService;
    }

    @Scheduled(fixedDelay = "24h") // Schedule this to run every hour
    public void sendPendingNotifications() {
        try {
            sessionRecommendedResourceService.processPendingResources();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}