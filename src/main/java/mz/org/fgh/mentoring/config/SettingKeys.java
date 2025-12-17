package mz.org.fgh.mentoring.config;

public final class SettingKeys {

    private SettingKeys() {}

    // =====================================================
    // Mentoring – Paths e diretórios
    // =====================================================
    public static final String RESOURCES_DIRECTORY =
            "mentoring.resources.directory";

    // =====================================================
    // Backend URL / Server
    // =====================================================
    public static final String SERVER_BASE_URL =
            "mentoring.server.baseUrl";

    // =====================================================
    // Rondas / Flow Engine
    // =====================================================
    public static final String RONDA_MENTEE_REMOVAL_INACTIVE_DAYS =
            "mentoring.ronda.removal.inactiveDays";

    public static final String FLOW_HISTORY_JOB_INTERVAL_MINUTES =
            "mentoring.flowHistory.job.intervalMinutes";

    public static final String FLOW_HISTORY_CRON =
            "mentoring.flowHistory.cron";

    public static final String FLOW_HISTORY_INTERRUPTION_ENABLED =
            "mentoring.flowHistory.interruptJob.enabled";

    // =====================================================
    // Inteligência Artificial
    // =====================================================
    public static final String AI_SESSION_SUMMARY_ENABLED =
            "mentoring.ai.sessionSummary.enabled";

    public static final String AI_PERFORMANCE_RISK_EVALUATION_ENABLED =
            "mentoring.ai.performanceRisk.enabled";

    // =====================================================
    // Password Reset
    // =====================================================
    public static final String PASSWORD_RESET_EXPIRATION_MINUTES =
            "mentoring.password.reset.token.expiration.time";

    public static final String PASSWORD_RESET_CLEANUP_CRON =
            "mentoring.password.reset.cleanup.cron";

}
