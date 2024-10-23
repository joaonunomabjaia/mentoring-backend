package mz.org.fgh.mentoring.service.form;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.form.FormQuestionDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.FormSection;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.Section;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.form.FormQuestionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.question.SectionService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class FormService {

    private FormRepository formRepository;

    private UserRepository userRepository;

    private FormQuestionRepository formQuestionRepository;
    @Inject
    private FormSectionService formSectionService;

    @Inject
    private SectionService sectionService;

    @Inject
    private TutorRepository tutorRepository;

    public FormService(UserRepository userRepository, FormRepository formRepository, FormQuestionRepository formQuestionRepository) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.formQuestionRepository = formQuestionRepository;
    }

    public Page<FormDTO> findAll(Pageable pageable) {
        // Fetch paginated forms from the repository
        Page<Form> formPage = this.formRepository.findAll(pageable);

        // Convert the Page<Form> to Page<FormDTO> by mapping the Form entities to DTOs
        return formPage.map(FormDTO::new);
    }


    public Optional<Form> findById(Long id){

        return this.formRepository.findById(id);
    }

    public List<FormDTO> findBySelectedFilter(final String code, String name, String programmaticAreaCode, String program){
        List<FormDTO> formDTOS = new ArrayList<>();
        List<Form> forms = this.formRepository.findBySelectedFilter(code, name, programmaticAreaCode, program);
        for(Form form : forms){
            List<FormSectionQuestion> formSectionQuestions = formQuestionRepository.fetchByForm(form.getId());
            //form.setFormQuestions(formSectionQuestions);
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

    public Page<FormDTO> search(final String code, final String name, final String program, final String programmaticArea, Pageable pageable)    {

        Page<Form> pageForm = this.formRepository.search(code, name, program, programmaticArea, pageable);

        return pageForm.map(this::formToDTO);
    }

    private FormDTO formToDTO(Form form){
        return new FormDTO(form);
    }

    public Form updateLifeCycleStatus(Form form, Long userId) {
        User user = this.userRepository.fetchByUserId(userId);
        Optional<Form> f =  this.formRepository.findByUuid(form.getUuid());
        if (f.isPresent()) {
            f.get().setLifeCycleStatus(form.getLifeCycleStatus());
            f.get().setUpdatedBy(user.getUuid());
            f.get().setUpdatedAt(DateUtils.getCurrentDate());
            this.formRepository.update(f.get());
            return f.get();
        }
        return null;
    }

    @Transactional
    public FormDTO saveOrUpdate(Long userId, FormDTO formDTO) {
        User user = this.userRepository.fetchByUserId(userId);
        Partner partner = user.getEmployee().getPartner();
        Form form = formDTO.toForm();
        form.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        form.setDescription(form.getName());
        form.setPartner(partner);
        List<FormQuestionDTO> formQuestions = formDTO.getFormQuestions();


        if(StringUtils.isEmpty(formDTO.getUuid())) {
            form.setUuid(Utilities.generateUUID());
            form.setCreatedBy(user.getUuid());
            form.setCreatedAt(DateUtils.getCurrentDate());
            form.setCode(generateFormCode(form));
            form.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            for (FormSection fs : form.getFormSections()) {
                fs.setForm(form);
                fs.setCreatedBy(user.getUuid());
                fs.setCreatedAt(DateUtils.getCurrentDate());
                fs.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                if (!existsOnDB(fs.getSection())) {
                    fs.getSection().setCreatedBy(user.getUuid());
                    fs.getSection().setCreatedAt(DateUtils.getCurrentDate());
                    fs.getSection().setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                }
                if (Utilities.listHasElements(fs.getFormSectionQuestions())) {
                    for (FormSectionQuestion fq : fs.getFormSectionQuestions()) {
                        fq.setFormSection(fs);
                        fq.setCreatedBy(user.getUuid());
                        fq.setCreatedAt(DateUtils.getCurrentDate());
                        fq.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                    }
                }
            }
            Form newForm = this.formRepository.save(form);
            return new FormDTO(newForm);
        }

        return formDTO;
    }

    public boolean existsOnDB(@NotNull Section section) {
        return sectionService.getByUUID(section.getUuid()).isPresent();
    }

    protected @NotEmpty String generateFormCode(Form form) {
        // Validate that the form has a valid Programmatic Area and Program
        if (form.getProgrammaticArea() == null || form.getProgrammaticArea().getProgram() == null) {
            throw new IllegalArgumentException("Form must be associated with a valid Programmatic Area and Program.");
        }

        // Retrieve the last created form for this program from the database
        Optional<Form> lastFormOpt = formRepository.findTopByProgramOrderByCreatedAtDesc(form.getProgrammaticArea().getProgram());

        String newFormCode;
        if (lastFormOpt.isPresent()) {
            // Extract the last form code and increment the sequential number
            Form lastForm = lastFormOpt.get();
            String lastFormCode = lastForm.getCode();

            // Extract the sequential number part from the last form code
            String[] parts = lastFormCode.split("-");
            int sequentialNumber;
            try {
                sequentialNumber = Integer.parseInt(parts[parts.length - 1]);
            } catch (NumberFormatException e) {
                // Handle the case where the last part of the code isn't a number
                throw new IllegalStateException("Invalid form code format: " + lastFormCode);
            }

            // Increment the sequential number
            sequentialNumber++;

            // Generate the new form code with "TC-" prefix and formatted sequential number
            newFormCode = String.format("TVC-%s-%04d", form.getProgrammaticArea().getProgram().getCode(), sequentialNumber);
        } else {
            // If no previous form exists, start the sequence with 1
            newFormCode = String.format("TVC-%s-0001", form.getProgrammaticArea().getProgram().getCode());
        }

        return newFormCode;
    }




    public List<Form> getByTutorUuid(String tutorUuid) {
        List<Form> forms = formRepository.getAllOfTutor(tutorRepository.findByUuid(tutorUuid).get());
        //forms.forEach(form -> form.setFormQuestions(formQuestionRepository.fetchByForm(form.getId())));
        return forms;
    }
}
