package mz.org.fgh.mentoring.service.form;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.form.FormType;
import mz.org.fgh.mentoring.repository.form.FormTypeRepository;

import java.util.List;

@Singleton
public class FormTypeService {
    @Inject
    private FormTypeRepository formTypeRepository;

    public List<FormType> findAll(){
        return this.formTypeRepository.findAll();
    }

}
