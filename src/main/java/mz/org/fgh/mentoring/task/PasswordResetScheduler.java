package mz.org.fgh.mentoring.task;

import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.service.tutor.PasswordResetService;
import mz.org.fgh.mentoring.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Date;

import static com.cronutils.model.CronType.QUARTZ;
import static mz.org.fgh.mentoring.config.SettingKeys.PASSWORD_RESET_CLEANUP_CRON;

@Singleton
public class PasswordResetScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetScheduler.class);
    private static final String DEFAULT_CRON = "0 0 2 * * ?"; // 02:00 diariamente

    private final SettingService settingService;
    private final PasswordResetService passwordResetService;

    private final CronParser cronParser =
            new CronParser(CronDefinitionBuilder.instanceDefinitionFor(QUARTZ));

    public PasswordResetScheduler(SettingService settingService,
                                  PasswordResetService passwordResetService) {
        this.settingService = settingService;
        this.passwordResetService = passwordResetService;
    }

    @Scheduled(fixedDelay = "1m")
    void controlledScheduler() {

        boolean enabled = settingService.isEnabled(PASSWORD_RESET_CLEANUP_CRON, true);
        if (!enabled) return;

        String cronExpr = settingService.get(PASSWORD_RESET_CLEANUP_CRON, DEFAULT_CRON);

        Cron cron;
        ExecutionTime executionTime;
        try {
            cron = cronParser.parse(cronExpr);
            executionTime = ExecutionTime.forCron(cron);
        } catch (Exception e) {
            LOG.error("Cron inválido para PasswordReset cleanup: {}", cronExpr, e);
            return;
        }

        ZonedDateTime now = ZonedDateTime.now()
                .withSecond(0)
                .withNano(0);

        try {
            if (executionTime.isMatch(now)) {
                Date currentDate = DateUtils.getCurrentDate();
                int deleted = passwordResetService.deleteExpiredUnused(currentDate);

                if (deleted > 0) {
                    LOG.info("PasswordResetScheduler: {} tokens expirados removidos.", deleted);
                } else {
                    LOG.debug("PasswordResetScheduler: nenhum token expirado para remover.");
                }
            }
        } catch (Exception e) {
            LOG.error("Falha ao executar PasswordResetScheduler (cron: {}).", cronExpr, e);
        }
    }
}
