package mz.org.fgh.mentoring.task;

import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.micronaut.context.event.ApplicationEventListener;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.tutored.PasswordReset;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.tutor.PasswordResetRepository;
import mz.org.fgh.mentoring.service.tutor.PasswordResetService;
import mz.org.fgh.mentoring.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class PasswordResetScheduler implements ApplicationEventListener<ApplicationStartupEvent> {

    private final PasswordResetRepository passwordResetRepository;
    private final SettingsRepository settingsRepository;
    private final PasswordResetService passwordResetService;

    private ScheduledExecutorService scheduler;

    public PasswordResetScheduler(PasswordResetRepository passwordResetRepository,
                                  SettingsRepository settingsRepository, PasswordResetService passwordResetService) {
        this.passwordResetRepository = passwordResetRepository;
        this.settingsRepository = settingsRepository;
        this.passwordResetService = passwordResetService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartupEvent event) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleNextRun();
    }

    private void scheduleNextRun() {
        int intervalMinutes = 10; // padrão caso setting não exista
        try {
            Setting setting = settingsRepository.findByDesignation("PASSWORD_RESET_CLEANUP_INTERVAL_MIN")
                    .orElseThrow(() -> new RuntimeException("Setting PASSWORD_RESET_CLEANUP_INTERVAL_MIN não configurado"));
            intervalMinutes = Integer.parseInt(setting.getValue());
        } catch (Exception e) {
            System.err.println("Erro ao obter intervalo de limpeza de tokens. Usando 10 minutos. " + e.getMessage());
        }

        scheduler.schedule(() -> {
            try {
                cleanupExpiredTokens();
            } catch (Exception e) {
                System.err.println("Erro ao limpar tokens expirados: " + e.getMessage());
            } finally {
                scheduleNextRun();
            }
        }, intervalMinutes, TimeUnit.MINUTES);
    }

    private void cleanupExpiredTokens() {
        Date now = DateUtils.getCurrentDate();
        List<PasswordReset> expiredTokens = passwordResetService.findByUsedFalseAndExpiresAtBefore(now);

        if (!expiredTokens.isEmpty()) {
            expiredTokens.forEach(token -> passwordResetRepository.delete(token));
            System.out.println("PasswordResetScheduler: " + expiredTokens.size() + " tokens expirados removidos.");
        }
    }

    @PreDestroy
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
