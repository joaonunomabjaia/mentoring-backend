package mz.org.fgh.mentoring.service.form;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.SampleQuestion;
import mz.org.fgh.mentoring.repository.tutor.FormRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.Arrays;
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

    public List<Form> findSampleIndicatorForms(){

        return this.formRepository.findSampleIndicatorForms(Arrays.asList(SampleQuestion.NUMBER_OF_COLLECTED_SAMPLES.getValue(),
                SampleQuestion.NUMBER_OF_REJECTED_SAMPLES.getValue(),
                SampleQuestion.NUMBER_OF_TRANSPORTED_SAMPLES.getValue(),
                SampleQuestion.NUMBER_OF_RECEIVED_SAMPLES.getValue()), LifeCycleStatus.ACTIVE);
    }

    public List<Form> findBySelectedFilter(final String code, String name, String programaticAreaCode, String partnerUUID ){

        return this.formRepository.findBySelectedFilter(code,name, programaticAreaCode, LifeCycleStatus.ACTIVE, partnerUUID);
    }

    public Form findByCode(String code){
        return this.formRepository.findByCode(code);
    }
    public Form create(Form form){
        return this.formRepository.save(form);
    }
    public Form update(Form form){
        return  this.formRepository.update(form);
    }
}
