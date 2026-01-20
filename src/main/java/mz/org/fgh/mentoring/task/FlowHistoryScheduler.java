package mz.org.fgh.mentoring.task;

import com.cronutils.model.Cron;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.cronutils.model.definition.CronDefinitionBuilder;

import static com.cronutils.model.CronType.QUARTZ;
import static mz.org.fgh.mentoring.config.SettingKeys.FLOW_HISTORY_CRON;
import static mz.org.fgh.mentoring.config.SettingKeys.FLOW_HISTORY_INTERRUPTION_ENABLED;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.service.tutored.MenteeFlowEngineService;
import mz.org.fgh.mentoring.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Optional;

@Singleton
public class FlowHistoryScheduler {

    @Inject
    private MenteeFlowEngineService menteeFlowEngineService;

    @Inject
    private UserService userService;

    @Inject
    private SettingService settingService;

    private final CronParser cronParser =
            new CronParser(CronDefinitionBuilder.instanceDefinitionFor(QUARTZ));

    private static final String DEFAULT_CRON = "0 0 2 * * ?";

    private static final Logger LOG = LoggerFactory.getLogger(FlowHistoryScheduler.class);

    // Executa a cada minuto e verifica se o cron dispara
    @Scheduled(fixedDelay = "1m")
    void controlledScheduler() {
        boolean enabled = settingService.getBoolean(FLOW_HISTORY_INTERRUPTION_ENABLED, false);
        if (!enabled) return;

        String cronExpr = settingService.get(FLOW_HISTORY_CRON, DEFAULT_CRON);
        Cron cron = cronParser.parse(cronExpr);
        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        // Trunca segundos/nanos para não depender do segundo exato
        ZonedDateTime now = ZonedDateTime.now()
                .withSecond(0)
                .withNano(0);

        try {
            if (executionTime.isMatch(now)) {
                User systemUser = userService.getSystemUser();
                menteeFlowEngineService.checkAndInterruptInactiveRounds(systemUser);
            }
        } catch (Exception e) {
            LOG.error("Falha ao executar o flow history scheduler: {}", cronExpr, e);
        }
    }

}
