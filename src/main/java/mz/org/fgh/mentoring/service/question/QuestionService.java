package mz.org.fgh.mentoring.service.question;

import io.micronaut.core.annotation.Creator;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.mentorship.EvaluationLocationRepository;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;
import mz.org.fgh.mentoring.repository.question.SectionRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.answer.AnswerService;
import mz.org.fgh.mentoring.service.form.FormSectionQuestionService;
import mz.org.fgh.mentoring.service.program.ProgramService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class QuestionService {
    @Inject
    private QuestionRepository questionRepository;
    @Inject
    private SectionRepository questionCategoryRepository;
    @Inject
    private UserRepository userRepository;

    @Inject
    private AnswerService answerService;

    @Inject
    private EvaluationLocationRepository evaluationLocationRepository;

    @Inject
    private FormSectionQuestionService formQuestionService ;
    @Inject
    private ProgramService programService;

    @Creator
    public QuestionService(){}

    public QuestionService(QuestionRepository questionRepository, SectionRepository sectionRepository, EvaluationLocationRepository evaluationLocationRepository) {
        this.questionRepository = questionRepository;
        this.questionCategoryRepository = sectionRepository;
        this.evaluationLocationRepository = evaluationLocationRepository;
    }


    public QuestionService(QuestionRepository questionRepository, UserRepository userRepository, EvaluationLocationRepository evaluationLocationRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.evaluationLocationRepository = evaluationLocationRepository;
    }

    public List<Question> getQuestionsByFormCode(String formCode) {
        return null;
    }

    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.getAllQuestions(LifeCycleStatus.ACTIVE);
        List<QuestionDTO> dtos = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO dto = new QuestionDTO(question);
            dtos.add(dto);
        }
        return dtos;
    }

    public Page<QuestionDTO> search(final String code, final String description, final Long programId, Pageable pageable) {
        // Fetch the program if the programId is valid
        Long validProgramId = null;
        if (programId != null && programId > 0) {
            validProgramId = programService.findById(programId).map(Program::getId).orElse(null);
        }

        // Perform the search using the repository
        Page<Question> pageQuestion = questionRepository.search(code, description, validProgramId, pageable);

        // Use Java streams to convert the list of Questions to QuestionDTOs
        return pageQuestion.map(this::questionToDTO);
    }


    public List<QuestionDTO> findAllQuestions() {
        List<Question> questionList = this.questionRepository.findAll();

        try {
            return Utilities.parseList(questionList,QuestionDTO.class);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Question create(Question question, Long userId) {
        User user = userRepository.findById(userId).get();
        question.setCreatedBy(user.getUuid());
        question.setCreatedAt(DateUtils.getCurrentDate());
        question.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        question.setCode(generateQuestionCode(question));

        return this.questionRepository.save(question);
    }

    protected @NotEmpty(message = "Code cannot be empty")
    @Size(max = 50, message = "Code cannot exceed 50 characters")
    String generateQuestionCode(Question question) {
        // Ensure the question has a valid Program
        if (question.getProgram() == null) {
            throw new IllegalArgumentException("Question must be associated with a valid Program.");
        }

        // Retrieve the last created question code for this program from the database
        Optional<Question> lastQuestionOpt = questionRepository.findTopByProgramOrderByCreatedAtDesc(question.getProgram());

        String newQuestionCode;
        if (lastQuestionOpt.isPresent()) {
            // Extract the last question code and increment the sequential number
            Question lastQuestion = lastQuestionOpt.get();
            String lastQuestionCode = lastQuestion.getCode();

            // Extract the sequential number part (assumed to be the numeric part at the end of the code)
            String[] parts = lastQuestionCode.split("-");
            int sequentialNumber;
            try {
                sequentialNumber = Integer.parseInt(parts[parts.length - 1]);
            } catch (NumberFormatException e) {
                // Handle the case where the last part of the code isn't a number
                throw new IllegalStateException("Invalid question code format: " + lastQuestionCode);
            }

            // Increment the sequential number
            sequentialNumber++;

            // Generate the new question code with "CPT-" prefix and six-digit sequence
            newQuestionCode = String.format("CPT-%s-%06d", question.getProgram().getCode(), sequentialNumber);
        } else {
            // If no previous question exists, start the sequence with 1
            newQuestionCode = String.format("CPT-%s-000001", question.getProgram().getCode());
        }

        return newQuestionCode;
    }


    public Optional<Question> findById(final Long id){
        return this.questionRepository.findById(id);
    }
    @Transactional
    public Question update(Question question, Long userId) {
        User user = userRepository.findById(userId).get();

//        Question question = questionRepository.findById(questionDTO.getId()).get();
        question.setUpdatedBy(user.getUuid());
        question.setUpdatedAt(DateUtils.getCurrentDate());

//        question.setQuestion(question.getQuestion());
//        question.setProgram(question.getProgram());

        return this.questionRepository.update(question);
    }

    public Question updateLifeCycleStatus(Question question, Long userId) {
        User user = this.userRepository.fetchByUserId(userId);
            question.setUpdatedBy(user.getUuid());
            question.setUpdatedAt(DateUtils.getCurrentDate());
            this.questionRepository.update(question);
            return question;
    }

    @Transactional
    public Question delete(Question question, Long userId) {
        User user = userRepository.findById(userId).get();
        question.setLifeCycleStatus(LifeCycleStatus.DELETED);
        question.setUpdatedBy(user.getUuid());
        question.setUpdatedAt(DateUtils.getCurrentDate());

        return this.questionRepository.update(question);
    }

    @Transactional
    public void destroy(Question question) {
        boolean hasAnswers = answerService.doesQuestionHaveAnswers(question);
        boolean hasFormQuestions = formQuestionService.doesQuestionHaveFormQuestions(question);
        if(!hasAnswers && !hasFormQuestions){
            this.questionRepository.delete(question);
        }
    }

    public Page<QuestionDTO> getByPageAndSize(Pageable pageable) {
        
        Page<Question> pageQuestion = this.questionRepository.findAll(pageable);

        List<Question> questionList = pageQuestion.getContent();

        List<QuestionDTO> questions = new ArrayList<QuestionDTO>();
        for (Question question: questionList) {
            questions.add(new QuestionDTO(question));
        }

        return pageQuestion.map(this::questionToDTO);
    }

    private QuestionDTO questionToDTO(Question question){
        return new QuestionDTO(question, this.existsInFormSectionQuestion(question));
    }

    public boolean existsInFormSectionQuestion(Question question){
        boolean resp = this.questionRepository.existsInFormSectionQuestion(question);

        return resp;
    };
}
