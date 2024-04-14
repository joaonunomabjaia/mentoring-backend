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
import mz.org.fgh.mentoring.repository.programaticarea.ProgramaticAreaRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
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
        User user = this.userRepository.fetchByUserId(userId);
        Partner partner = user.getEmployee().getPartner();
        Form form = formDTO.toForm();
        form.setDescription(form.getName());
        form.setPartner(partner);
        List<FormQuestionDTO> formQuestions = formDTO.getFormQuestions();
        Date currDate = new Date();

        if(StringUtils.isEmpty(formDTO.getUuid())) {
            form.setUuid(Utilities.generateUUID());
            form.setCreatedBy(user.getUuid());
            form.setCreatedAt(currDate);
            form.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            Form newForm = this.formRepository.save(form);
            List<FormQuestion> newFormQuestions = new ArrayList<>();
            for (FormQuestionDTO dto : formQuestions) {
                FormQuestion formQuestion = dto.toFormQuestion();
                formQuestion.setCreatedBy(user.getUuid());
                formQuestion.setCreatedAt(currDate);
                formQuestion.setForm(newForm);
                formQuestion.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                FormQuestion newFQ = this.formQuestionRepository.save(formQuestion);
                newFormQuestions.add(newFQ);
            }
            newForm.setFormQuestions(newFormQuestions);
            return new FormDTO(newForm);
        }

        List<FormQuestion> listOfFormQuestions = new ArrayList<>();
        for (FormQuestionDTO dto : formQuestions) {
            if(dto.getId()==null) {
                FormQuestion formQuestion = dto.toFormQuestion();
                formQuestion.setCreatedBy(user.getUuid());
                formQuestion.setCreatedAt(currDate);
                formQuestion.setForm(form);
                formQuestion.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                FormQuestion newFQ = this.formQuestionRepository.save(formQuestion);
                listOfFormQuestions.add(newFQ);
            } else {
                listOfFormQuestions.add(dto.toFormQuestion());
            }
        }
            form.setUpdatedBy(user.getUuid());
            form.setUpdatedAt(currDate);
            form.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            Form updatedForm = this.formRepository.update(form);
            updatedForm.setFormQuestions(listOfFormQuestions);
            return new FormDTO(updatedForm);
    }
}
