package mz.org.fgh.mentoring.service.form;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.form.FormQuestionDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.SampleQuestion;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.form.FormQuestionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class FormService {

    private FormRepository formRepository;
    private UserRepository userRepository;

    private FormQuestionRepository formQuestionRepository;

    public FormService(UserRepository userRepository, FormRepository formRepository, FormQuestionRepository formQuestionRepository) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.formQuestionRepository = formQuestionRepository;
    }

    public List<FormDTO> findAll(long limit, long offset){

        List<FormDTO> formDTOS = new ArrayList<>();
        List<Form> forms = new ArrayList<>();

        if(limit > 0){
            forms = this.formRepository.findFormWithLimit(limit, offset);
        }else{
            forms = this.formRepository.findAll();
        }

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

    public List<FormDTO> findBySelectedFilter(final String code, String name, String programmaticAreaCode){
        List<FormDTO> formDTOS = new ArrayList<>();
        List<Form> forms = this.formRepository.findBySelectedFilter(code, name, programmaticAreaCode, LifeCycleStatus.ACTIVE);
        for(Form form : forms){
            List<FormQuestion> formQuestions = formQuestionRepository.fetchByForm(form.getId(), LifeCycleStatus.ACTIVE);
            form.setFormQuestions(formQuestions);
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

    public List<FormDTO> search(final String code, final String name, final String programmaticArea) {
        List<Form> formList = this.formRepository.search(code, name, programmaticArea);
        List<FormDTO> forms = new ArrayList<FormDTO>();
        for (Form form: formList) {
            forms.add(new FormDTO(form));
        }
        return forms;
    }

    public FormDTO saveOrUpdate(Long userId, FormDTO formDTO) {
        User user = this.userRepository.findById(userId).get();
        Form form = formDTO.toForm();
        List<FormQuestionDTO> formQuestions = new ArrayList<>();
        List<FormQuestionDTO> addedFormQuestionDTOs = formDTO.getAddedFormQuestionDTOs();
        List<FormQuestionDTO> removedFormQuestionDTOs = formDTO.getRemovedFormQuestionDTOs();
        if(addedFormQuestionDTOs!=null && !addedFormQuestionDTOs.isEmpty()) {
            for (FormQuestionDTO formQuestionDTO : addedFormQuestionDTOs) {
                FormQuestion formQuestion = formQuestionDTO.toFormQuestion();
                if (form.getId() != null && formQuestion.getId() != null) {
                    formQuestion.setUpdatedAt(new Date());
                    formQuestion.setUpdatedBy(user.getUuid());
                    this.formQuestionRepository.update(formQuestion);
                    form.setUpdatedAt(new Date());
                    form.setUpdatedBy(user.getUuid());
                    form = this.formRepository.update(form);
                    formQuestions.add(new FormQuestionDTO(formQuestion));
                } else if (form.getId() == null) {
                    formQuestion.setCreatedAt(new Date());
                    formQuestion.setCreatedBy(user.getUuid());
                    this.formQuestionRepository.save(formQuestion);
                    form.setCreatedAt(new Date());
                    form.setCreatedBy(user.getUuid());
                    form = this.create(form);
                    formQuestions.add(new FormQuestionDTO(formQuestion));
                }
            }
        }
        if(removedFormQuestionDTOs!=null && !removedFormQuestionDTOs.isEmpty()) {
            for (FormQuestionDTO formQuestionDTO : removedFormQuestionDTOs) {
                FormQuestion formQuestion = formQuestionDTO.toFormQuestion();
                formQuestion.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
                formQuestion.setUpdatedAt(new Date());
                formQuestion.setUpdatedBy(user.getUuid());
                this.formQuestionRepository.update(formQuestion);
                form.setUpdatedAt(new Date());
                form.setUpdatedBy(user.getUuid());
                this.formRepository.update(form);
            }
        }
        FormDTO dto = new FormDTO(form);
        dto.setFormQuestions(formQuestions);
        return dto;
    }
}
