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
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.session.SessionStatus;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.report.SessionSummary;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.form.FormQuestionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.CabinetRepository;
import mz.org.fgh.mentoring.repository.mentorship.DoorRepository;
import mz.org.fgh.mentoring.repository.mentorship.MentorshipRepository;
import mz.org.fgh.mentoring.repository.question.EvaluationTypeRepository;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaRepository;
import mz.org.fgh.mentoring.repository.session.SessionRepository;
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

@Singleton
public class MentorshipService {

    private final MentorshipRepository mentorshipRepository;
    private final SessionRepository sessionRepository;
    private final FormRepository formRepository;
    private final TutorRepository tutorRepository;
    private final TutoredRepository tutoredRepository;
    private final CabinetRepository cabinetRepository;
    private final HealthFacilityRepository healthFacilityRepository;
    private final EvaluationTypeRepository evaluationTypeRepository;
    private final DoorRepository doorRepository;
    private final RondaRepository rondaRepository;
    private final SessionStatusRepository sessionStatusRepository;
    private final QuestionRepository questionRepository;
    private final FormQuestionRepository formQuestionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(MentorshipService.class);

    @Inject
    public MentorshipService(MentorshipRepository mentorshipRepository, SessionRepository sessionRepository,
                             FormRepository formRepository, TutorRepository tutorRepository,
                             TutoredRepository tutoredRepository, CabinetRepository cabinetRepository,
                             HealthFacilityRepository healthFacilityRepository, EvaluationTypeRepository evaluationTypeRepository,
                             DoorRepository doorRepository, RondaRepository rondaRepository,
                             SessionStatusRepository sessionStatusRepository, QuestionRepository questionRepository,
                             FormQuestionRepository formQuestionRepository, AnswerRepository answerRepository,
                             UserRepository userRepository) {
        this.mentorshipRepository = mentorshipRepository;
        this.sessionRepository = sessionRepository;
        this.formRepository = formRepository;
        this.tutorRepository = tutorRepository;
        this.tutoredRepository = tutoredRepository;
        this.cabinetRepository = cabinetRepository;
        this.healthFacilityRepository = healthFacilityRepository;
        this.evaluationTypeRepository = evaluationTypeRepository;
        this.doorRepository = doorRepository;
        this.rondaRepository = rondaRepository;
        this.sessionStatusRepository = sessionStatusRepository;
        this.questionRepository = questionRepository;
        this.formQuestionRepository = formQuestionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public List<Session> synchronizeMentorships(List<Session> sessions) {
        for (Session session : sessions) {
            Optional<Session> optSession = sessionRepository.findByUuid(session.getUuid());
            if (optSession.isEmpty()) {
                saveNewSessionWithMentorships(session);
            }
        }
        return sessions;
    }

    private void saveNewSessionWithMentorships(Session session) {
        Session newSession = sessionRepository.save(session);
        for (Mentorship mentorship : session.getMentorships()) {
            mentorship.setSession(newSession);
            mentorship.setForm(formRepository.findByUuid(mentorship.getForm().getUuid()).orElseThrow());
            mentorship.setTutor(tutorRepository.findByUuid(mentorship.getTutor().getUuid()).orElseThrow());
            mentorship.setTutored(tutoredRepository.findByUuid(mentorship.getTutored().getUuid()).orElseThrow());
            mentorship.setCabinet(cabinetRepository.findByUuid(mentorship.getCabinet().getUuid()).orElseThrow());
            mentorshipRepository.save(mentorship);
        }
    }

    @Transactional
    public List<MentorshipDTO> saveMentorships(List<MentorshipDTO> mentorshipDTOS, Long userId) {
        List<MentorshipDTO> savedMentorships = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow();

        for (MentorshipDTO mentorshipDTO : mentorshipDTOS) {
            if (mentorshipDTO.getSession() != null) {
                saveMentorshipWithSession(mentorshipDTO, user, savedMentorships);
            }
        }
        return savedMentorships;
    }

    private void saveMentorshipWithSession(MentorshipDTO mentorshipDTO, User user, List<MentorshipDTO> savedMentorships) {
        SessionDTO sessionDTO = mentorshipDTO.getSession();
        Session session = sessionRepository.findByUuid(sessionDTO.getUuid()).orElse(sessionDTO.getSession());

        try {
            Form form = getForm(mentorshipDTO);
            Tutored tutored = getTutored(mentorshipDTO);
            SessionStatus sessionStatus = getSessionStatus(sessionDTO);
            Ronda ronda = getRonda(sessionDTO);

            if (session.getId() != null) {
                updateSession(session, sessionStatus, form, tutored, ronda, user);
            } else {
                createNewSession(session, sessionStatus, form, tutored, ronda, user);
            }

            saveMentorship(mentorshipDTO, session, form, tutored, user, savedMentorships);
        } catch (Exception exception) {
            LOG.error(exception.getMessage());
            throw new RuntimeException("Error while running MentorshipService", exception);
        }
    }

    private Form getForm(MentorshipDTO mentorshipDTO) {
        Form form = formRepository.findByUuid(mentorshipDTO.getForm().getUuid()).orElseThrow();
        form.setFormQuestions(null);
        form.setAnswers(null);
        return form;
    }

    private Tutored getTutored(MentorshipDTO mentorshipDTO) {
        Tutored tutored = tutoredRepository.findByUuid(mentorshipDTO.getMentee().getUuid()).orElseThrow();
        tutored.getEmployee().setLocations(null);
        return tutored;
    }

    private SessionStatus getSessionStatus(SessionDTO sessionDTO) {
        return sessionStatusRepository.findByUuid(sessionDTO.getSessionStatus().getUuid()).orElseThrow();
    }

    private Ronda getRonda(SessionDTO sessionDTO) {
        Ronda ronda = rondaRepository.findByUuid(sessionDTO.getRonda().getUuid()).orElseThrow();
        ronda.setRondaMentees(null);
        ronda.setRondaMentors(null);
        return ronda;
    }

    private void updateSession(Session session, SessionStatus sessionStatus, Form form, Tutored tutored, Ronda ronda, User user) {
        session.setStatus(sessionStatus);
        session.setForm(form);
        session.setMentee(tutored);
        session.setRonda(ronda);
        session.setUpdatedBy(user.getUuid());
        session.setUpdatedAt(DateUtils.getCurrentDate());
        sessionRepository.update(session);
    }

    private void createNewSession(Session session, SessionStatus sessionStatus, Form form, Tutored tutored, Ronda ronda, User user) {
        session.setStatus(sessionStatus);
        session.setForm(form);
        session.setMentee(tutored);
        session.setRonda(ronda);
        session.setCreatedBy(user.getUuid());
        session.setCreatedAt(DateUtils.getCurrentDate());
        sessionRepository.save(session);
    }

    private void saveMentorship(MentorshipDTO mentorshipDTO, Session session, Form form, Tutored tutored, User user, List<MentorshipDTO> savedMentorships) {
        Optional<Mentorship> optionalMentorship = mentorshipRepository.findByUuid(mentorshipDTO.getMentorship().getUuid());
        Mentorship mentorship = optionalMentorship.orElseGet(mentorshipDTO::getMentorship);

        if (mentorship.getId() != null) {
            mentorship.setUpdatedBy(user.getUuid());
            mentorship.setUpdatedAt(DateUtils.getCurrentDate());
            Mentorship savedMentorship = mentorshipRepository.update(mentorship);
            savedMentorships.add(new MentorshipDTO(savedMentorship));
            if (session.getRonda().isRondaZero()) {
                mentorship.getTutored().setZeroEvaluationDone(mentorshipDTO.getMentee().isZeroEvaluationDone());
                mentorship.getTutored().setZeroEvaluationScore(mentorshipDTO.getMentee().getZeroEvaluationScore());
                tutoredRepository.update(mentorship.getTutored());
            }
        } else {
            createNewMentorship(mentorshipDTO, session, form, tutored, user, savedMentorships, mentorship);
        }
    }

    private void createNewMentorship(MentorshipDTO mentorshipDTO, Session session, Form form, Tutored tutored, User user, List<MentorshipDTO> savedMentorships, Mentorship mentorship) {
        mentorship.setSession(session);
        mentorship.setForm(form);
        mentorship.setTutor(getTutor(mentorshipDTO));
        mentorship.setTutored(tutored);
        mentorship.setCabinet(getCabinet(mentorshipDTO));
        mentorship.setDoor(getDoor(mentorshipDTO));
        mentorship.setEvaluationType(getEvaluationType(mentorshipDTO));
        mentorship.setCreatedBy(user.getUuid());
        mentorship.setCreatedAt(DateUtils.getCurrentDate());

        Mentorship savedMentorship = mentorshipRepository.save(mentorship);
        saveMentorshipAnswers(savedMentorship, form, mentorshipDTO.getAnswers(), user);
        savedMentorships.add(new MentorshipDTO(savedMentorship));
        if (session.getRonda().isRondaZero()) {
            mentorship.getTutored().setZeroEvaluationDone(mentorshipDTO.getMentee().isZeroEvaluationDone());
            mentorship.getTutored().setZeroEvaluationScore(mentorshipDTO.getMentee().getZeroEvaluationScore());
            tutoredRepository.update(mentorship.getTutored());
        }
    }

    public List<SessionSummary> generateSessionSummary(Session session) {
        List<SessionSummary> summaries = new ArrayList<>();

        for (Mentorship mentorship : session.getMentorships()) {
            if (mentorship.isPatientEvaluation()) {
                for (Answer answer : mentorship.getAnswers()) {
                    String cat = answer.getQuestion().getQuestionCategory().getCategory();
                    if (categoryAlreadyExists(cat, summaries)){
                        doCountInCategory(cat, summaries, answer);
                    } else {
                        summaries.add(initSessionSummary(answer));
                    }
                }
                break;
            }
        }
        return summaries;
    }

    private SessionSummary initSessionSummary(Answer answer) {
        SessionSummary sessionSummary = new SessionSummary();
        sessionSummary.setTitle(answer.getQuestion().getQuestionCategory().getCategory());

        if (answer.getValue().equals("SIM")) {
            sessionSummary.setSimCount(sessionSummary.getSimCount() + 1);
        } else if (answer.getValue().equals("NAO")) {
            sessionSummary.setNaoCount(sessionSummary.getNaoCount() + 1);
        }
        return sessionSummary;
    }

    private void doCountInCategory(String cat, List<SessionSummary> summaries, Answer answer) {
        for (SessionSummary sessionSummary : summaries) {
            if (sessionSummary.getTitle().equals(cat)) {
                if (answer.getValue().equals("SIM")) {
                    sessionSummary.setSimCount(sessionSummary.getSimCount() + 1);
                } else if (answer.getValue().equals("NAO")) {
                    sessionSummary.setNaoCount(sessionSummary.getNaoCount() + 1);
                }
            }
        }
    }

    private boolean categoryAlreadyExists(String cat, List<SessionSummary> summaries) {
        for (SessionSummary sessionSummary : summaries) {
            if (sessionSummary.getTitle().equals(cat)) {
                return true;
            }
        }
        return false;
    }

    private Tutor getTutor(MentorshipDTO mentorshipDTO) {
        Tutor tutor = tutorRepository.findByUuid(mentorshipDTO.getMentor().getUuid()).orElseThrow();
        tutor.getEmployee().setLocations(null);
        tutor.setTutorProgrammaticAreas(null);
        return tutor;
    }

    private Cabinet getCabinet(MentorshipDTO mentorshipDTO) {
        return cabinetRepository.findByUuid(mentorshipDTO.getCabinet().getUuid()).orElseThrow();
    }

    private Door getDoor(MentorshipDTO mentorshipDTO) {
        return doorRepository.findByUuid(mentorshipDTO.getDoor().getUuid()).orElseThrow();
    }

    private EvaluationType getEvaluationType(MentorshipDTO mentorshipDTO) {
        return evaluationTypeRepository.findByUuid(mentorshipDTO.getEvaluationType().getUuid()).orElseThrow();
    }

    private List<Answer> saveMentorshipAnswers(Mentorship mentorship, Form form, List<AnswerDTO> answerDTOS, User user) {
        List<Answer> answers = new ArrayList<>();
        for (AnswerDTO answerDTO : answerDTOS) {
            Answer answer = answerDTO.getAnswer();
            answer.setForm(form);
            answer.setMentorship(mentorship);
            answer.setQuestion(questionRepository.findByUuid(answerDTO.getQuestion().getUuid()).orElseThrow());
            answer.setCreatedBy(user.getUuid());
            answer.setCreatedAt(DateUtils.getCurrentDate());
            answers.add(answerRepository.save(answer));
        }
        return answers;
    }
}
