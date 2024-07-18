package mz.org.fgh.mentoring.service.session;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.base.BaseService;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.repository.session.SessionStatusRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Inject
    private EmailService emailService;

    @Inject
    public SessionService(SessionRepository sessionRepository, RondaRepository rondaRepository,
                          AnswerRepository answerRepository, MentorshipRepository mentorshipRepository,
                          UserRepository userRepository, SessionStatusRepository sessionStatusRepository,
                          TutorRepository tutorRepository, TutoredRepository tutoredRepository,
                          FormRepository formRepository) {
        this.sessionRepository = sessionRepository;
        this.rondaRepository = rondaRepository;
        this.answerRepository = answerRepository;
        this.mentorshipRepository = mentorshipRepository;
        this.userRepository = userRepository;
        this.sessionStatusRepository = sessionStatusRepository;
        this.tutorRepository = tutorRepository;
        this.tutoredRepository = tutoredRepository;
        this.formRepository = formRepository;
    }

    public List<Session> getAllRondas(List<String> rondasUuids) {
        List<Ronda> rondas = rondaRepository.findRondasByUuids(rondasUuids);
        List<Session> sessions = new ArrayList<>();
        for (Ronda ronda : rondas) {
            List<Session> sessionList = sessionRepository.findAllOfRonda(ronda.getId());
            for (Session session : sessionList) {
                session.setMentorships(mentorshipRepository.fetchBySessionUuid(session.getUuid(), LifeCycleStatus.ACTIVE));
                session.getMentorships().forEach(mentorship -> mentorship.setAnswers(answerRepository.fetchByMentorshipUuid(mentorship.getUuid(), LifeCycleStatus.ACTIVE)));
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
                variables.put("menteesName", session.getMentee().getEmployee().getFullName());
                variables.put("mentorName", session.getRonda().getRondaMentors().get(0).getMentor().getEmployee().getFullName());
                variables.put("date", session.getStartDate().toString() );

                String populatedHtml = emailService.populateTemplateVariables(htmlTemplate, variables);

                emailService.sendEmail(session.getMentee().getEmployee().getEmail(), "Notificacão de Agenda de Sessão de Mentoria", populatedHtml); // Send an email for the resource

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
