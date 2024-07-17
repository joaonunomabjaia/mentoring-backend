package mz.org.fgh.mentoring.service.session;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailService;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.*;

@Singleton
public class SessionService {

    SessionRepository sessionRepository;

    RondaRepository rondaRepository;

    AnswerRepository answerRepository;

    MentorshipRepository mentorshipRepository;

    TutorRepository tutorRepository;

    @Inject
    private EmailService emailService;

    public SessionService(SessionRepository sessionRepository, RondaRepository rondaRepository, AnswerRepository answerRepository, MentorshipRepository mentorshipRepository, TutorRepository tutorRepository) {
        this.sessionRepository = sessionRepository;
        this.rondaRepository = rondaRepository;
        this.answerRepository = answerRepository;
        this.mentorshipRepository = mentorshipRepository;
        this.tutorRepository = tutorRepository;
    }

    public List<Session> getAllRondas(List<String> rondasUuids) {
        List<Ronda> rondas = rondaRepository.findRondasByUuids(rondasUuids);
        List<Session> sessions = new ArrayList<>();
        for (Ronda ronda: rondas) {
            Optional<Session> optSession = sessionRepository.findByRonda(ronda.getId());
            if(optSession.isPresent()) {
                Session session = optSession.get();
                List<Mentorship> mentorships = this.mentorshipRepository.fetchBySessionUuid(session.getUuid(), LifeCycleStatus.ACTIVE);
                for (Mentorship mentorship : mentorships) {
                    mentorship.setAnswers(answerRepository.fetchByMentorshipUuid(mentorship.getUuid(), LifeCycleStatus.ACTIVE));
                }
                session.setMentorships(mentorships);
                sessions.add(session);
            }
        }
        return sessions;
    }

    public Session findByUuid(String uuid) {
        return sessionRepository.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("Session not found"));
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
