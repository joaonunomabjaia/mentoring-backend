package mz.org.fgh.mentoring.service.form;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutor.FormDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.SampleQuestion;
import mz.org.fgh.mentoring.repository.tutor.FormRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class FormService {

    @Inject
    FormRepository formRepository;

    public List<FormDTO> findAll(){

        List<FormDTO> formDTOS = new ArrayList<>();

        List<Form> forms = this.formRepository.findAll();

        for(Form form : forms){
            formDTOS.add(new FormDTO(form));
        }
        return formDTOS;
    }

    public Optional<Form> findById(Long id){

        return this.formRepository.findById(id);
    }

    public List<FormDTO> findSampleIndicatorForms(){

        List<FormDTO> formDTOS = new ArrayList<>();

        List<Form> forms =  this.formRepository.findSampleIndicatorForms(Arrays.asList(SampleQuestion.NUMBER_OF_COLLECTED_SAMPLES.getValue(),
                SampleQuestion.NUMBER_OF_REJECTED_SAMPLES.getValue(),
                SampleQuestion.NUMBER_OF_TRANSPORTED_SAMPLES.getValue(),
                SampleQuestion.NUMBER_OF_RECEIVED_SAMPLES.getValue()), LifeCycleStatus.ACTIVE);

        for(Form form : forms){
            formDTOS.add(new FormDTO(form));
        }

        return formDTOS;
    }

    public List<FormDTO> findBySelectedFilter(final String code, String name, String programaticAreaCode, String partnerUUID ){

        List<FormDTO> formDTOS = new ArrayList<>();

        List<Form> forms = this.formRepository.findBySelectedFilter(code,name, programaticAreaCode, LifeCycleStatus.ACTIVE, partnerUUID);

        for(Form form : forms){
            formDTOS.add(new FormDTO(form));
        }

        return formDTOS;
    }

    public FormDTO findByCode(String code){

        Form form = this.formRepository.findByCode(code);
        return new FormDTO(form);
    }

    public List<FormDTO> findFormByProgrammaticAreaUuid(String programaticAreaUuid){

        List<FormDTO> formDTOS = new ArrayList<>();

        List<Form> forms = this.formRepository.findFormByProgrammaticAreaUuid(programaticAreaUuid);

        for(Form form : forms){
            formDTOS.add(new FormDTO(form));
        }

        return formDTOS;
    }
    public Form create(Form form){
        return this.formRepository.save(form);
    }
    public Form update(Form form){
        return  this.formRepository.update(form);
    }
}
