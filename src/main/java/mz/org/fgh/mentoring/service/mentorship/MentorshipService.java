package mz.org.fgh.mentoring.service.mentorship;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.answer.AnswerDTO;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.dto.session.SessionDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.mentorship.Door;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.session.SessionStatus;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.error.MentoringBusinessException;
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
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.PerformedSession;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public MentorshipService(MentorshipRepository mentorshipRepository){
        this.mentorshipRepository = mentorshipRepository;
    }

    public Mentorship createMentorship(Mentorship mentorship){
        if(StringUtils.isEmpty(mentorship.getCode()) && mentorship.getTutor() == null && mentorship.getTutored() == null){
            throw new MentoringBusinessException("Fields 'CODE', 'TUTOR' and 'TUTORED' are required.");
        }
        return mentorshipRepository.save(mentorship);
    }

    public List<Session> synchronizeMentorships(final List<Session> sessions) {
        for (final Session session : sessions) {
            Session se = this.sessionRepository.findByUuid(session.getUuid());

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

    public Mentorship findMentorshipById(@NotNull Long id){
        Optional<Mentorship> optionalMentorship = mentorshipRepository.findById(id);
        if(optionalMentorship.isEmpty()){
            throw new MentoringBusinessException("Mentorship with ID: "+id+" was not found.");
        }
        return optionalMentorship.get();
    }

    public List<Mentorship> findAllMentorships(){
        return mentorshipRepository.findAll();
    }

    public List<PerformedSession> findPerformedSessionsBySelectedFilterPMQTRList(Date startDate, Date endDate) {
        return this.mentorshipRepository.getSelectedOfFilterPMQTRList(startDate, endDate);
    }

    public List<Mentorship> fetchBySelectedFilter(String code, String tutor, String tutored,
                                                  String formName, String healthFacility, String iterationType,
                                                  Integer iterationNumber, String lifeCycleStatus, Date performedStartDate,
                                                  Date performedEndDate) {
        LifeCycleStatus lfStatus = null;
        if(lifeCycleStatus != null) {
            try {
                lfStatus = LifeCycleStatus.valueOf(lifeCycleStatus.toUpperCase());
            } catch(IllegalArgumentException iae) {
                // Ignore
            }
        }

        return this.mentorshipRepository.fetchBySelectedFilter(code, tutor, tutored, formName, healthFacility, iterationType, iterationNumber, lfStatus, performedStartDate, performedEndDate);
    }

    public List<MentorshipDTO> saveMentorships(List<MentorshipDTO> mentorshipDTOS) {
        List<MentorshipDTO> savedMentorships = new ArrayList<>();
        for (MentorshipDTO mentorshipDTO: mentorshipDTOS) {
            if(mentorshipDTO.getSession()!=null) {
                SessionDTO sessionDTO = mentorshipDTO.getSession();
                Session session = sessionRepository.findByUuid(sessionDTO.getUuid());
                if(session==null) {
                    session = sessionDTO.getSession();
                    session.setStatus(sessionStatusRepository.findByUuid(session.getStatus().getUuid()).get());
                    sessionRepository.save(session);
                }

                Mentorship mentorship = mentorshipDTO.getMentorship();
                mentorship.setSession(session);
                mentorship.setForm(formRepository.findByUuid(mentorshipDTO.getForm().getUuid()).get());
                mentorship.setTutor(tutorRepository.findByUuid(mentorshipDTO.getMentor().getUuid()).get());
                mentorship.setTutored(tutoredRepository.findByUuid(mentorshipDTO.getMentee().getUuid()).get());
                mentorship.setCabinet(cabinetRepository.findByUuid(mentorshipDTO.getCabinet().getUuid()).get());
                mentorship.setDoor(doorRepository.findByUuid(mentorshipDTO.getDoor().getUuid()).get());
                mentorship.setEvaluationType(evaluationTypeRepository.findByUuid(mentorshipDTO.getEvaluationType().getUuid()).get());
                Mentorship savedMentorship = mentorshipRepository.save(mentorship);
                List<Answer> answers = saveMentorshipAnswers(savedMentorship, mentorshipDTO.getAnswers());
                savedMentorship.setAnswers(answers);

                MentorshipDTO dto = new MentorshipDTO(savedMentorship);
                savedMentorships.add(dto);
            }
        }
        return savedMentorships;
    }

    private List<Answer> saveMentorshipAnswers(Mentorship mentorship, List<AnswerDTO> answerDTOS) {
        List<Answer> answers = new ArrayList<>();
        for (AnswerDTO answerDTO: answerDTOS) {
            Answer answer = answerDTO.getAnswer();
            answer.setForm(mentorship.getForm());
            answer.setMentorship(mentorship);
            answer.setQuestion(questionRepository.findByUuid(answerDTO.getQuestion().getUuid()).get());
            Answer savedAnswer = answerRepository.save(answer);
            answers.add(savedAnswer);
        }
        return answers;
    }
}
