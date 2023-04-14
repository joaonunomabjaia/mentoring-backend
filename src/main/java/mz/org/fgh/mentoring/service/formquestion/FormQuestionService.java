package mz.org.fgh.mentoring.service.formquestion;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.repository.tutor.FormQuestionRepository;
import mz.org.fgh.mentoring.repository.tutor.FormRepository;

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

    public FormQuestion create(FormQuestion formQuestion){
        return this.formQuestionRepository.save(formQuestion);
    }
}
