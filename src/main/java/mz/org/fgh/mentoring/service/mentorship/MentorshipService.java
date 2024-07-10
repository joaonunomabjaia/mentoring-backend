package mz.org.fgh.mentoring.service.mentorship;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.answer.AnswerDTO;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.dto.session.SessionDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.mentorship.Door;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.session.SessionStatus;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.form.FormQuestionRepository;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.CabinetRepository;
import mz.org.fgh.mentoring.repository.mentorship.DoorRepository;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import mz.org.fgh.mentoring.repository.question.EvaluationTypeRepository;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.session.SessionStatusRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Singleton
public class MentorshipService {

    private final MentorshipRepository mentorshipRepository;
    @Inject
    private SessionRepository sessionRepository;

    @Inject
    FormRepository formRepository;

    @Inject
    TutorRepository tutorRepository;

    @Inject
    TutoredRepository tutoredRepository;

    @Inject
    CabinetRepository cabinetRepository;

    @Inject
    HealthFacilityRepository healthFacilityRepository;

    @Inject
    private EvaluationTypeRepository evaluationTypeRepository;

    @Inject
    private DoorRepository doorRepository;

    @Inject
    private RondaRepository rondaRepository;

    @Inject
    private SessionStatusRepository sessionStatusRepository;

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private FormQuestionRepository formQuestionRepository;

    @Inject
    private AnswerRepository answerRepository;

    @Inject
    private UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(MentorshipService.class);

    public MentorshipService(MentorshipRepository mentorshipRepository){
        this.mentorshipRepository = mentorshipRepository;
    }

    public List<Session> synchronizeMentorships(final List<Session> sessions) {
        for (final Session session : sessions) {
            Optional<Session> optSession = this.sessionRepository.findByUuid(session.getUuid());
            Session se = optSession.isPresent() ? optSession.get() : null;

            if (se == null) {
                Session newSession = sessionRepository.save(session);
                for (final Mentorship mentorship : session.getMentorships()) {
                    mentorship.setSession(newSession);
                    mentorship.setForm(formRepository.findByUuid(mentorship.getForm().getUuid()).get());
                    mentorship.setTutor(tutorRepository.findByUuid(mentorship.getTutor().getUuid()).get());
                    mentorship.setTutored(tutoredRepository.findByUuid(mentorship.getTutored().getUuid()).get());
                    mentorship.setCabinet(cabinetRepository.findByUuid(mentorship.getCabinet().getUuid()).get());

                    mentorshipRepository.save(mentorship);
                }

            }
        }
        return sessions;
    }

    @Transactional
    public List<MentorshipDTO> saveMentorships(List<MentorshipDTO> mentorshipDTOS, Long userId) {
        List<MentorshipDTO> savedMentorships = new ArrayList<>();
        for (MentorshipDTO mentorshipDTO: mentorshipDTOS) {
            if(mentorshipDTO.getSession()!=null) {
                SessionDTO sessionDTO = mentorshipDTO.getSession();

                Optional<Session> optionalSession = sessionRepository.findByUuid(sessionDTO.getUuid());
                Session session =  optionalSession.isPresent() ? optionalSession.get() : null;

                try {

                    Optional<Form> optForm = formRepository.findByUuid(mentorshipDTO.getForm().getUuid());
                    Form form = optForm.get();
                    form.setFormQuestions(null);
                    form.setAnswers(null);

                    Optional<Tutored> optTutored = tutoredRepository.findByUuid(mentorshipDTO.getMentee().getUuid());
                    Tutored tutored = optTutored.get();
                    tutored.getEmployee().setLocations(null);

                    User user = userRepository.findById(userId).get();
                    Optional<SessionStatus> optSessionStatus = sessionStatusRepository.findByUuid(sessionDTO.getSessionStatus().getUuid());
                    SessionStatus sessionStatus = optSessionStatus.get();

                    Optional<Ronda> optionalRonda = rondaRepository.findByUuid(sessionDTO.getRonda().getUuid());
                    Ronda ronda = optionalRonda.get();
                    ronda.setRondaMentees(null);
                    ronda.setRondaMentors(null);

                if(session!=null) {
                    session.setStatus(sessionStatus);
                    session.setForm(form);
                    session.setMentee(tutored);
                    session.setRonda(ronda);
                    session.setUpdatedBy(user.getUuid());
                    session.setUpdatedAt(DateUtils.getCurrentDate());
                    sessionRepository.update(session);
                }
                else {
                    session = sessionDTO.getSession();
                    session.setStatus(sessionStatus);
                    session.setForm(form);
                    session.setMentee(tutored);
                    session.setRonda(ronda);
                    session.setCreatedBy(user.getUuid());
                    session.setCreatedAt(DateUtils.getCurrentDate());
                    sessionRepository.save(session);
                }

                Optional<Mentorship> optionalMentorship = this.mentorshipRepository.findByUuid(mentorshipDTO.getMentorship().getUuid());
                Mentorship mentorship = optionalMentorship.isPresent() ? optionalMentorship.get() : null;

                if(mentorship!=null) {
                    mentorship.setUpdatedBy(user.getUuid());
                    mentorship.setUpdatedAt(DateUtils.getCurrentDate());
                    Mentorship savedMentorship = mentorshipRepository.update(mentorship);
                    MentorshipDTO dto = new MentorshipDTO(savedMentorship);
                    savedMentorships.add(dto);
                }
                else {
                    mentorship = mentorshipDTO.getMentorship();
                    mentorship.setSession(session);
                    mentorship.setForm(form);
                    Optional<Tutor> optTutor = tutorRepository.findByUuid(mentorshipDTO.getMentor().getUuid());
                    Tutor tutor = optTutor.get();
                    tutor.getEmployee().setLocations(null);
                    tutor.setTutorProgrammaticAreas(null);
                    mentorship.setTutor(tutor);
                    mentorship.setTutored(tutored);
                    Optional<Cabinet> optCabinet = cabinetRepository.findByUuid(mentorshipDTO.getCabinet().getUuid());
                    Cabinet cabinet = optCabinet.get();
                    mentorship.setCabinet(cabinet);
                    Optional<Door> optDoor = doorRepository.findByUuid(mentorshipDTO.getDoor().getUuid());
                    Door door = optDoor.get();
                    mentorship.setDoor(door);
                    Optional<EvaluationType> optEvaluationType = evaluationTypeRepository.findByUuid(mentorshipDTO.getEvaluationType().getUuid());
                    EvaluationType evaluationType = optEvaluationType.get();
                    mentorship.setEvaluationType(evaluationType);
                    mentorship.setCreatedBy(user.getUuid());
                    mentorship.setCreatedAt(DateUtils.getCurrentDate());
                    Mentorship savedMentorship = mentorshipRepository.save(mentorship);
                    saveMentorshipAnswers(savedMentorship, form, mentorshipDTO.getAnswers(), user);
                    MentorshipDTO dto = new MentorshipDTO(savedMentorship);
                    savedMentorships.add(dto);
                }
                }
                catch (Exception exception) {
                    LOG.error(exception.getMessage());
                    throw new RuntimeException("Error while running MentorshipService");
                }
            }
        }
        return savedMentorships;
    }

    private List<Answer> saveMentorshipAnswers(Mentorship mentorship, Form form, List<AnswerDTO> answerDTOS, User user) throws RuntimeException {
        List<Answer> answers = new ArrayList<>();
        for (AnswerDTO answerDTO: answerDTOS) {
            Answer answer = answerDTO.getAnswer();
            answer.setForm(form);
            answer.setMentorship(mentorship);
            Optional<Question> optionalQuestion = questionRepository.findByUuid(answerDTO.getQuestion().getUuid());
            Question question = optionalQuestion.get();
            answer.setQuestion(question);
            answer.setCreatedBy(user.getUuid());
            answer.setCreatedAt(DateUtils.getCurrentDate());
            Answer savedAnswer = answerRepository.save(answer);
            answers.add(savedAnswer);
        }
        return answers;
    }
}
