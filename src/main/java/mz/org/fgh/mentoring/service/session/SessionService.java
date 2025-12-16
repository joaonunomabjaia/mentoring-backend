package mz.org.fgh.mentoring.service.session;

import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.base.BaseService;
import mz.org.fgh.mentoring.dto.session.SessionReportDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationType;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaType;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import mz.org.fgh.mentoring.repository.question.EvaluationTypeRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaTypeRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.repository.session.SessionStatusRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.ai.AiSummaryService;
import mz.org.fgh.mentoring.service.resource.ResourceService;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.*;

import static mz.org.fgh.mentoring.config.SettingKeys.SERVER_BASE_URL;

@Singleton
public class SessionService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;
    private final RondaRepository rondaRepository;
    private final AnswerRepository answerRepository;
    private final MentorshipRepository mentorshipRepository;
    private final UserRepository userRepository;
    private final SessionStatusRepository sessionStatusRepository;
    private final TutorRepository tutorRepository;
    private final TutoredRepository tutoredRepository;
    private final FormRepository formRepository;
    private final ResourceService resourceService;
    private final RondaTypeRepository rondaTypeRepository;
    private final SettingService settings;

    @Inject
    private EmailService emailService;
    @Inject
    AiSummaryService aiSummaryService;

    @Inject
    private SettingsRepository settingsRepository;
    @Inject
    private EvaluationTypeRepository evaluationTypeRepository;


    public SessionService(SessionRepository sessionRepository, RondaRepository rondaRepository,
                          AnswerRepository answerRepository, MentorshipRepository mentorshipRepository,
                          UserRepository userRepository, SessionStatusRepository sessionStatusRepository,
                          TutorRepository tutorRepository, TutoredRepository tutoredRepository,
                          FormRepository formRepository, ResourceService resourceService, RondaTypeRepository rondaTypeRepository, SettingService settings) {
        this.sessionRepository = sessionRepository;
        this.rondaRepository = rondaRepository;
        this.answerRepository = answerRepository;
        this.mentorshipRepository = mentorshipRepository;
        this.userRepository = userRepository;
        this.sessionStatusRepository = sessionStatusRepository;
        this.tutorRepository = tutorRepository;
        this.tutoredRepository = tutoredRepository;
        this.formRepository = formRepository;
        this.resourceService = resourceService;
        this.rondaTypeRepository = rondaTypeRepository;
        this.settings = settings;
    }

    public List<Session> getAllRondas(List<String> rondasUuids) {
        List<Ronda> rondas = rondaRepository.findRondasByUuids(rondasUuids);
        List<Session> sessions = new ArrayList<>();
        for (Ronda ronda : rondas) {
            Set<Session> sessionList = sessionRepository.findAllOfRonda(ronda.getId());
            for (Session session : sessionList) {
                session.setMentorships(mentorshipRepository.fetchBySessionUuid(session.getUuid(), LifeCycleStatus.ACTIVE));
                if (Utilities.listHasElements(session.getMentorships())) {
                    for (Mentorship mentorship : session.getMentorships()) {
                        mentorship.setAnswers(answerRepository.fetchByMentorshipUuid(mentorship.getUuid(), LifeCycleStatus.ACTIVE));
                    }
                }
                //session.getMentorships().forEach(mentorship -> mentorship.setAnswers(answerRepository.fetchByMentorshipUuid(mentorship.getUuid(), LifeCycleStatus.ACTIVE)));
                sessions.add(session);
            }
        }
        return sessions;
    }

    public Session findByUuid(String uuid) {
        return sessionRepository.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("Session not found"));
    }

    public List<Session> createOrUpdate(List<Session> sessions, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Session> newSessions = new ArrayList<>();
        for (Session session : sessions) {
            Optional<Session> onDbSession = sessionRepository.findByUuid(session.getUuid());
            populateNewSessionFields(session);
            if (onDbSession.isPresent()) {
                session.setId(onDbSession.get().getId());
                addUpdateAuditInfo(session, onDbSession.get(), user);
                sessionRepository.update(session);
                LOG.info("Updated Session: {}", session);
            } else {
                addCreationAuditInfo(session, user);
                sessionRepository.save(session);
                LOG.info("Created Session: {}", session);
            }
            newSessions.add(session);
        }
        return newSessions;
    }

    private void populateNewSessionFields(Session session) {
        session.setStatus(sessionStatusRepository.findByUuid(session.getStatus().getUuid()).orElseThrow());
        session.setForm(formRepository.findByUuid(session.getForm().getUuid()).orElseThrow());
        session.setRonda(rondaRepository.findByUuid(session.getRonda().getUuid()).orElseThrow());
        session.setMentee(tutoredRepository.findByUuid(session.getMentee().getUuid()).orElseThrow());
    }

    @Transactional
    public void processPendingSessions() throws MessagingException {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date startDate = cal.getTime();

        List<Session> sessions = sessionRepository.getAllOfRondaPending(startDate);

        if (!Utilities.listHasElements(sessions)) return;

        for (Session session : sessions){

            try {
                String htmlTemplate = emailService.loadHtmlTemplate("emailNotificationTemplate");

                Map<String, String> variables = new HashMap<>();
                variables.put("serverUrl", settings.get(SERVER_BASE_URL, "https://mentdev.csaude.org.mz"));
                variables.put("menteesName", session.getMentee().getEmployee().getFullName());
                variables.put("mentorName", session.getRonda().getActiveMentor().getEmployee().getFullName());
                variables.put("date", DateUtils.formatToDDMMYYYY(session.getNextSessionDate()));

                String populatedHtml = emailService.populateTemplateVariables(htmlTemplate, variables);

                emailService.sendEmail(session.getMentee().getEmployee().getEmail(), "Notificação de Sessão de Mentoria Agendada", populatedHtml); // Send an email for the resource

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public SessionReportDTO getSessionReport() {
        long totalSessions = sessionRepository.countAllActiveSessions();
        long totalInternal = sessionRepository.countActiveSessionsByRondaTypeCode("MENTORIA_INTERNA");
        long totalExternal = sessionRepository.countActiveSessionsByRondaTypeCode("MENTORIA_EXTERNA");

        return new SessionReportDTO(totalSessions, totalInternal, totalExternal);
    }

    public List<String> getWeakPointsSummaryForMentee(Long tutoredId) {
        return sessionRepository.findWeakPointsFromLastSessionByTutoredId(tutoredId);
    }

    public String generateWeakPointsSummary(Mentorship mentorship) {
        List<Answer> weakAnswers = answerRepository.findWeakAnswersByMentorshipId(mentorship.getId());

        if (weakAnswers == null || weakAnswers.isEmpty()) {
            return "Nenhum ponto fraco encontrado para este mentorado.";
        }

        String menteeName = mentorship.getTutored().getEmployee().getFullName();
        StringBuilder context = new StringBuilder();

        for (Answer answer : weakAnswers) {
            context.append("- ").append(answer.getQuestion().getQuestion()).append("\n");
        }

        // 🟩 Obtém o programa da sessão
        String programName = mentorship.getForm().getProgrammaticArea().getProgram().getCode();

        // 🟩 Filtra os recursos do mesmo programa
        List<Map<String, String>> recommendedResources = resourceService.extractResourceSummariesByProgram(programName);

        return aiSummaryService.summarizeWeakPoints(menteeName, context.toString());
    }



    public void generateSummary() {
        List<Session> sessions = sessionRepository.findAllWithoutSummary();

        if (!Utilities.listHasElements(sessions)) {
            LOG.info("No sessions without summary found. Skipping summary generation.");
            return;
        }
        LOG.info("Generating summary for {} sessions", sessions.size());

        for (Session session : sessions) {
            try {
                EvaluationType evaluationType = evaluationTypeRepository.getByCode("Consulta");
                Pageable pageable = Pageable.from(0, 1);
                List<Mentorship> result = mentorshipRepository.fetchLatestBySessionAndEvaluationType(session, evaluationType, pageable);
                Mentorship latest = result.isEmpty() ? null : result.get(0);

                String summary = generateWeakPointsSummary(latest);
                session.setSessionSummary(summary);
                sessionRepository.update(session);
                LOG.info("Resumo gerado e salvo para sessão UUID: {}", session.getUuid());
            } catch (Exception e) {
                LOG.error("Erro ao gerar resumo para a sessão UUID: {}", session.getUuid(), e);
            }
        }
    }

    @Transactional
    public void evaluatePerformanceRiskPerRonda() {
        RondaType rondaType = rondaTypeRepository.findByCode("MENTORIA_INTERNA");
        List<Long> rondaIds = rondaRepository.findAllRondaIds(rondaType);

        for (Long rondaId : rondaIds) {
            List<Session> sessions = sessionRepository.findAllWithSummaryByRondaId(rondaId);

            if (sessions.isEmpty()) continue;

            Map<Long, List<Session>> sessionsByMentee = new HashMap<>();

            for (Session session : sessions) {
                sessionsByMentee
                        .computeIfAbsent(session.getMentee().getId(), k -> new ArrayList<>())
                        .add(session);
            }

            for (List<Session> menteeSessions : sessionsByMentee.values()) {
                menteeSessions.sort(Comparator.comparing(Session::getStartDate));

                if (menteeSessions.size() < 2) continue;

                // Coleta os resumos das sessões anteriores (exclui a última)
                List<String> summaries = new ArrayList<>();
                for (int i = 0; i < menteeSessions.size(); i++) {
                    summaries.add(menteeSessions.get(i).getSessionSummary());
                }

                // Última sessão da ronda (a que vai receber o resultado)
                Session targetSession = menteeSessions.get(menteeSessions.size() - 1);
                String menteeName = targetSession.getMentee().getEmployee().getFullName();

                String evaluation = aiSummaryService.evaluatePerformanceRisk(menteeName, summaries);
                targetSession.setPerformanceRisk(evaluation);
                sessionRepository.update(targetSession);

                LOG.info("Risco salvo para sessão UUID {}", targetSession.getUuid());
            }

        }
    }


}
