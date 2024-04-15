package mz.org.fgh.mentoring.service.form;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.form.FormQuestionDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.form.FormQuestionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class FormQuestionService {

    FormQuestionRepository formQuestionRepository;

    private UserRepository userRepository;

    FormRepository formRepository;

    public FormQuestionService(FormQuestionRepository formQuestionRepository, UserRepository userRepository, FormRepository formRepository) {
        this.formQuestionRepository = formQuestionRepository;
        this.userRepository = userRepository;
        this.formRepository = formRepository;
    }

    public List<FormQuestion> findAll(){
        return this.formQuestionRepository.findAll();
    }

    public Optional<FormQuestion> findById(Long id){
        return this.formQuestionRepository.findById(id);
    }

    public List<FormQuestionDTO> fetchByForm(final Long formId){
        List<FormQuestion> formQuestions = this.formQuestionRepository.fetchByForm(formId , LifeCycleStatus.ACTIVE);
        List<FormQuestionDTO> formQuestionDTOS = new ArrayList<>();
        for (FormQuestion formQuestion : formQuestions) {
            FormQuestionDTO formQuestionDTO = new FormQuestionDTO(formQuestion);
            formQuestionDTOS.add(formQuestionDTO);
        }
     return formQuestionDTOS;
    }

    public List<FormQuestion> fetchByTutor(final Tutor tutor){
        return  this.formQuestionRepository.fetchByTutor( tutor, LifeCycleStatus.ACTIVE);
    }

    public FormQuestion create(FormQuestion formQuestion){
        return this.formQuestionRepository.save(formQuestion);
    }

    public void inactivate(Long userId, Long formId, FormQuestionDTO formQuestionDTO) {
        User user = this.userRepository.findById(userId).get();
        Form form = this.formRepository.findById(formId).get();
        FormQuestion formQuestion = formQuestionDTO.toFormQuestion();
        formQuestion.setForm(form);
        formQuestion.setUpdatedBy(user.getUuid());
        formQuestion.setUpdatedAt(new Date());
        formQuestion.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
        this.formQuestionRepository.update(formQuestion);
    }

}
