package mz.org.fgh.mentoring.service.form;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.repository.tutor.FormRepository;

import java.util.List;
import java.util.Optional;

@Singleton
public class FormService {

    @Inject
    FormRepository formRepository;

    public List<Form> findAll(){
        return this.formRepository.findAll();
    }

    public Optional<Form> findById(Long id){
        return this.formRepository.findById(id);
    }

    public Form findByCode(String code){
        return this.formRepository.findByCode(code);
    }
    public Form create(Form form){
        return this.formRepository.save(form);
    }
}
