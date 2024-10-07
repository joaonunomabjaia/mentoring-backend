package mz.org.fgh.mentoring.service.form;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.form.FormQuestionDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.answer.AnswerRepository;
import mz.org.fgh.mentoring.repository.form.FormQuestionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class FormQuestionService {

    FormQuestionRepository formQuestionRepository;

    private UserRepository userRepository;

    FormRepository formRepository;

    @Inject
    private AnswerRepository answerRepository;

    public FormQuestionService(FormQuestionRepository formQuestionRepository, UserRepository userRepository, FormRepository formRepository) {
        this.formQuestionRepository = formQuestionRepository;
        this.userRepository = userRepository;
        this.formRepository = formRepository;
    }

    public Page<FormSectionQuestion> findAll(Pageable pageable) {
        return formQuestionRepository.findAll(pageable);
    }

    public Optional<FormSectionQuestion> findById(Long id){
        return this.formQuestionRepository.findById(id);
    }

    public Page<FormQuestionDTO> fetchByForm(final Long formId, Pageable pageable) {
        // Fetch paginated FormQuestions from the repository
        Page<FormSectionQuestion> formQuestions = this.formQuestionRepository.fetchByForm(formId, pageable);

        // Convert the Page<FormQuestion> to Page<FormQuestionDTO> by mapping entities to DTOs
        return formQuestions.map(FormQuestionDTO::new);
    }


    public List<FormSectionQuestion> fetchByTutor(final Tutor tutor){
        return  this.formQuestionRepository.fetchByTutor( tutor, LifeCycleStatus.ACTIVE);
    }

    public FormSectionQuestion create(FormSectionQuestion formSectionQuestion){
        return this.formQuestionRepository.save(formSectionQuestion);
    }

    public void inactivate(Long userId, Long formId, FormQuestionDTO formQuestionDTO) {
        User user = this.userRepository.findById(userId).get();
        Form form = this.formRepository.findById(formId).get();
        FormSectionQuestion formSectionQuestion = formQuestionDTO.getFormQuestion();
        formSectionQuestion.setUpdatedBy(user.getUuid());
        formSectionQuestion.setUpdatedAt(new Date());
        formSectionQuestion.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
        this.formQuestionRepository.update(formSectionQuestion);
    }

    public List<FormSectionQuestion> fetchByFormsUuids(final List<String> formsUuids, Long offset, Long limit){
        if (offset > 0) offset = offset/limit;

        Pageable pageable = Pageable.from(Math.toIntExact(offset), Math.toIntExact(limit));
        return formQuestionRepository.findByFormsUuids(formsUuids, pageable);
    }

    public boolean doesQuestionHaveFormQuestions(Question question) {
        List<Answer> answers = this.answerRepository.getByQuestionId(question.getId());
        return !answers.isEmpty();
    }

    public List<FormSectionQuestion> fetchByFormsUuidsAndPageAndSize(final List<String> formsUuids, Long page, Long size){

        Pageable pageable = Pageable.from(Math.toIntExact(page), Math.toIntExact(size));
        return formQuestionRepository.findByFormsUuids(formsUuids, pageable);
    }
}
