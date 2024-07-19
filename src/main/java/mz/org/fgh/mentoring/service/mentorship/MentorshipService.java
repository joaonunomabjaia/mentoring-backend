package mz.org.fgh.mentoring.service.mentorship;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.base.BaseService;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.location.CabinetRepository;
import mz.org.fgh.mentoring.repository.mentorship.DoorRepository;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import mz.org.fgh.mentoring.repository.question.EvaluationTypeRepository;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MentorshipService extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(MentorshipService.class);

    private final MentorshipRepository mentorshipRepository;
    private final SessionRepository sessionRepository;
    private final FormRepository formRepository;
    private final TutorRepository tutorRepository;
    private final TutoredRepository tutoredRepository;
    private final CabinetRepository cabinetRepository;
    private final EvaluationTypeRepository evaluationTypeRepository;
    private final DoorRepository doorRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Inject
    public MentorshipService(MentorshipRepository mentorshipRepository, SessionRepository sessionRepository,
                             FormRepository formRepository, TutorRepository tutorRepository,
                             TutoredRepository tutoredRepository, CabinetRepository cabinetRepository,
                             EvaluationTypeRepository evaluationTypeRepository,
                             DoorRepository doorRepository, QuestionRepository questionRepository,
                             AnswerRepository answerRepository, UserRepository userRepository) {
        this.mentorshipRepository = mentorshipRepository;
        this.sessionRepository = sessionRepository;
        this.formRepository = formRepository;
        this.tutorRepository = tutorRepository;
        this.tutoredRepository = tutoredRepository;
        this.cabinetRepository = cabinetRepository;
        this.evaluationTypeRepository = evaluationTypeRepository;
        this.doorRepository = doorRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Mentorship> createMentorships(List<Mentorship> mentorships, Long userInfo) {
        List<Mentorship> createdMentorshipList = new ArrayList<>();
        User user = userRepository.findById(userInfo).orElseThrow();

        for (Mentorship mentorship : mentorships) {
            populateMentorshipFields(mentorship);
            addCreationAuditInfo(mentorship, user);
            mentorshipRepository.save(mentorship);
            saveAnswers(mentorship, user);
            createdMentorshipList.add(mentorship);
            LOG.info("Created mentorship: {}", mentorship);
        }
        return createdMentorshipList;
    }

    private void populateMentorshipFields(Mentorship mentorship) {
        mentorship.setSession(sessionRepository.findByUuid(mentorship.getSession().getUuid()).orElseThrow());
        mentorship.setTutor(tutorRepository.findByUuid(mentorship.getTutor().getUuid()).orElseThrow());
        mentorship.setTutored(tutoredRepository.findByUuid(mentorship.getTutored().getUuid()).orElseThrow());
        mentorship.setForm(formRepository.findByUuid(mentorship.getForm().getUuid()).orElseThrow());
        mentorship.setCabinet(cabinetRepository.findByUuid(mentorship.getCabinet().getUuid()).orElseThrow());
        mentorship.setEvaluationType(evaluationTypeRepository.findByUuid(mentorship.getEvaluationType().getUuid()).orElseThrow());
        mentorship.setDoor(doorRepository.findByUuid(mentorship.getDoor().getUuid()).orElseThrow());
    }

    private void saveAnswers(Mentorship mentorship, User user) {
        for (Answer answer : mentorship.getAnswers()) {
            answer.setMentorship(mentorship);
            answer.setQuestion(questionRepository.findByUuid(answer.getQuestion().getUuid()).orElseThrow());
            answer.setForm(mentorship.getForm());
            addCreationAuditInfo(answer, user);
            answerRepository.save(answer);
            LOG.info("Saved answer: {}", answer);
        }
    }

    public List<Mentorship> findAllOfSession(Session session) {
        return mentorshipRepository.findBySession(session);
    }
}
