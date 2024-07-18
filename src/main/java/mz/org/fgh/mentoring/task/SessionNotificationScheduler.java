package mz.org.fgh.mentoring.task;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.service.session.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;

@Singleton
public class SessionNotificationScheduler {

    public static final Logger LOG = LoggerFactory.getLogger(SessionNotificationScheduler.class);

    private final SessionService sessionService;


    public SessionNotificationScheduler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedDelay = "24h") // Schedule this to run every hour
    public void sendPendingNotifications(){

        try {
            sessionService.processPendingSessions();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
