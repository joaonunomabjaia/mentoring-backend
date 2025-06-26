package mz.org.fgh.mentoring.service.question;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.error.RecordInUseException;
import mz.org.fgh.mentoring.repository.form.FormSectionQuestionRepository;
import mz.org.fgh.mentoring.repository.question.QuestionRepository;
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
    private ProgramService programService;

    private final FormSectionQuestionRepository  formSectionQuestionRepository;

    @Creator
    public QuestionService(FormSectionQuestionRepository formSectionQuestionRepository){
        this.formSectionQuestionRepository = formSectionQuestionRepository;
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
    public Question create(Question question) {
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

    public Page<Question> findAll(@Nullable Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    public Page<Question> searchByName(String name, Pageable pageable) {
        return questionRepository.findByQuestionIlikeOrTableCodeIlike("%" + name + "%", "%" + name + "%", pageable);
    }

    @Transactional
    public Question update(Question question) {
        Optional<Question> existing = questionRepository.findByUuid(question.getUuid());
        if (existing.isEmpty()) {
            throw new RuntimeException("Question not found with UUID: " + question.getUuid());
        }

        Question toUpdate = existing.get();
        toUpdate.setQuestion(question.getQuestion());
        toUpdate.setTableCode(question.getTableCode());
        toUpdate.setProgram(question.getProgram());
        toUpdate.setUpdatedAt(DateUtils.getCurrentDate());
        toUpdate.setUpdatedBy(question.getUpdatedBy());

        return questionRepository.update(toUpdate);
    }

    @Transactional
    public void delete(String uuid) {
        Optional<Question> existing = questionRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            throw new RuntimeException("Question not found with UUID: " + uuid);
        }

        Question question = existing.get();

        // Exemplo de verificação futura:
         long count = formSectionQuestionRepository.countByQuestion(question);
         if (count > 0) {
             throw new RecordInUseException("A pergunta não pode ser eliminada porque está associada a outros registos.");
         }

        questionRepository.delete(question);
    }

    @Transactional
    public Question updateLifeCycleStatus(String uuid, LifeCycleStatus newStatus, String userUuid) {
        Optional<Question> existing = questionRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            throw new RuntimeException("Question not found with UUID: " + uuid);
        }

        Question question = existing.get();
        question.setLifeCycleStatus(newStatus);
        question.setUpdatedAt(DateUtils.getCurrentDate());
        question.setUpdatedBy(userUuid);

        return questionRepository.update(question);
    }


}
