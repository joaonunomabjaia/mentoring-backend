package mz.org.fgh.mentoring.service.formquestion;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.repository.tutor.FormQuestionRepository;
import mz.org.fgh.mentoring.repository.tutor.FormRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.List;
import java.util.Optional;

@Singleton
public class FormQuestionService {

    @Inject
    FormQuestionRepository formQuestionRepository;
    public List<FormQuestion> findAll(){
        return this.formQuestionRepository.findAll();
    }

    public Optional<FormQuestion> findById(Long id){
        return this.formQuestionRepository.findById(id);
    }

    public List<FormQuestion> fetchByForm(final Form form){
     return this.formQuestionRepository.fetchByForm(form.getId() , LifeCycleStatus.ACTIVE);
    }

    public List<FormQuestion> fetchByTutor(final Tutor tutor){
        return  this.formQuestionRepository.fetchByTutor( tutor, LifeCycleStatus.ACTIVE);
    }

    public FormQuestion create(FormQuestion formQuestion){
        return this.formQuestionRepository.save(formQuestion);
    }

}
