package mz.org.fgh.mentoring.service.form;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.form.FormSectionQuestionDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.form.FormSectionQuestionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.mentorship.EvaluationLocationRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class FormSectionQuestionService {

    FormSectionQuestionRepository formSectionQuestionRepository;

    private UserRepository userRepository;

    FormRepository formRepository;

    @Inject
    private AnswerRepository answerRepository;

    @Inject
    private final EvaluationLocationRepository evaluationLocationRepository;

    public FormSectionQuestionService(FormSectionQuestionRepository formSectionQuestionRepository, UserRepository userRepository, FormRepository formRepository, EvaluationLocationRepository evaluationLocationRepository) {
        this.formSectionQuestionRepository = formSectionQuestionRepository;
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.evaluationLocationRepository = evaluationLocationRepository;
    }

    public Page<FormSectionQuestion> findAll(Pageable pageable) {
        return formSectionQuestionRepository.findAll(pageable);
    }

    public Optional<FormSectionQuestion> findById(Long id){
        return this.formSectionQuestionRepository.findById(id);
    }

    public Page<FormSectionQuestionDTO> fetchByForm(final Long formId, Pageable pageable) {
        // Fetch paginated FormQuestions from the repository
        Page<FormSectionQuestion> formQuestions = this.formSectionQuestionRepository.fetchByForm(formId, pageable);

        // Convert the Page<FormQuestion> to Page<FormQuestionDTO> by mapping entities to DTOs
        return formQuestions.map(this::formSectionQuestionToDTO);
    }

    public boolean existsInMentorShipOrAnswer(FormSectionQuestion formSectionQuestion){
        boolean resp = this.formSectionQuestionRepository.existsInMentorShipOrAnswer(formSectionQuestion);

        return resp;
    };

     public FormSectionQuestionDTO formSectionQuestionToDTO (FormSectionQuestion formSectionQuestion) {
        return new FormSectionQuestionDTO(formSectionQuestion, existsInMentorShipOrAnswer(formSectionQuestion));
     }


    public FormSectionQuestion create(FormSectionQuestion formSectionQuestion){
        return this.formSectionQuestionRepository.save(formSectionQuestion);
    }

    public void inactivate(Long userId, Long formId, FormSectionQuestionDTO formQuestionDTO) {
        User user = this.userRepository.findById(userId).get();
        FormSectionQuestion formSectionQuestion = new FormSectionQuestion(formQuestionDTO);
        formSectionQuestion.setUpdatedBy(user.getUuid());
        formSectionQuestion.setUpdatedAt(new Date());
        formSectionQuestion.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
        this.formSectionQuestionRepository.update(formSectionQuestion);
    }

    public List<FormSectionQuestion> fetchByFormsUuids(final List<String> formsUuids, Long offset, Long limit){
        if (offset > 0) offset = offset/limit;

        Pageable pageable = Pageable.from(Math.toIntExact(offset), Math.toIntExact(limit));
        return formSectionQuestionRepository.findByFormsUuids(formsUuids, pageable);
    }

    public boolean doesQuestionHaveFormQuestions(Question question) {
        List<Answer> answers = this.answerRepository.getByQuestionId(question.getId());
        return !answers.isEmpty();
    }

    public List<FormSectionQuestion> fetchByFormsUuidsAndPageAndSize(final List<String> formsUuids, Long page, Long size){

        Pageable pageable = Pageable.from(Math.toIntExact(page), Math.toIntExact(size));
        return formSectionQuestionRepository.findByFormsUuids(formsUuids, pageable);
    }

    @Transactional
    public void destroy(FormSectionQuestion formSectionQuestion) {
        this.formSectionQuestionRepository.delete(formSectionQuestion);
    }
}
