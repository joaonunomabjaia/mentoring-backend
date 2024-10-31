package mz.org.fgh.mentoring.service.form;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.form.FormSectionDTO;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.form.FormSectionQuestionDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.FormSection;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.ResponseType;
import mz.org.fgh.mentoring.entity.question.Section;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.form.FormSectionQuestionRepository;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.form.FormSectionRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.question.SectionService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityNotFoundException;
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

    private FormSectionQuestionRepository formQuestionRepository;
    @Inject
    private FormSectionService formSectionService;

    @Inject
    private SectionService sectionService;

    @Inject
    private TutorRepository tutorRepository;
    @Inject
    private FormSectionQuestionRepository formSectionQuestionRepository;
    @Inject
    private FormSectionRepository formSectionRepository;

    public FormService(UserRepository userRepository, FormRepository formRepository, FormSectionQuestionRepository formQuestionRepository) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.formQuestionRepository = formQuestionRepository;
    }

    public Page<FormDTO> findAll(Pageable pageable) {
        // Fetch paginated forms from the repository
        Page<Form> formPage = this.formRepository.findAllWithFormSections(pageable);

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

    private List<FormSection> formSectionsDTOtoFormSections(List<FormSectionDTO> formSectionDTOS, Form form, User user){
        List<FormSection> formSections = new ArrayList<>();

        for (FormSectionDTO formSectionDTO : formSectionDTOS) {// Novas formSections e antigas
            FormSection formSection = new FormSection(formSectionDTO);
            formSection.setForm(form);

            Optional<FormSection> optionalFormSection = formSectionRepository.findByUuid(formSection.getUuid());
            if (optionalFormSection.isPresent()) {// formSection ja existia
                formSection = optionalFormSection.get();
                formSection.getSection().setUpdatedBy(user.getUuid());
                formSection.getSection().setUpdatedAt(DateUtils.getCurrentDate());
                formSection.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                formSectionRepository.update(formSection);
            } else { // Nova formSection
                formSection.setFormSectionQuestions(new ArrayList<>());
                formSection.setUuid(Utilities.generateUUID());
                formSection.setCreatedBy(user.getUuid());
                formSection.setCreatedAt(DateUtils.getCurrentDate());
                formSection.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                formSectionRepository.save(formSection); // Grava para posteriormente associar a Questions caso necessario
            }

            for (FormSectionQuestionDTO formSectionQuestionDTO : formSectionDTO.getFormSectionQuestions()) {
                if (formSectionQuestionDTO == null) {
                    continue;
                }

                Optional<FormSectionQuestion> formSectionQuestion = formSectionQuestionRepository.findByUuid(formSectionQuestionDTO.getUuid());
                if(!formSectionQuestion.isPresent()){ // O formSectionQuestion e' novo
                    FormSectionQuestion formSectionQuestionNew = new FormSectionQuestion(formSectionQuestionDTO);
                    formSectionQuestionNew.setUuid(Utilities.generateUUID());
                    formSectionQuestionNew.setCreatedBy(user.getUuid());
                    formSectionQuestionNew.setCreatedAt(DateUtils.getCurrentDate());
                    formSectionQuestionNew.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                    formSectionQuestionNew.setFormSection(formSection);
                    formSectionQuestionNew.setQuestion(new Question(formSectionQuestionDTO.getQuestionDTO()));

                    formSection.getFormSectionQuestions().add(formSectionQuestionRepository.save(formSectionQuestionNew));
                } else { // O formSectionQuestion ja existia
                    FormSectionQuestion formSectionQuestionOld = formSectionQuestion.get();
                    formSectionQuestionOld.setSequence(formSectionQuestionDTO.getSequence());
                    formSectionQuestionOld.setEvaluationType(new EvaluationType(formSectionQuestionDTO.getEvaluationType()));
                    formSectionQuestionOld.setResponseType(new ResponseType(formSectionQuestionDTO.getResponseType()));
                    formSectionQuestionOld.setUpdatedBy(user.getUuid());
                    formSectionQuestionOld.setUpdatedAt(DateUtils.getCurrentDate());
                    formSection.getFormSectionQuestions().add(formSectionQuestionRepository.update(formSectionQuestionOld));
                }
            }

            formSections.add(formSection);
        }

        return formSections;
    }

    @Transactional
    public FormDTO saveOrUpdate(Long userId, FormDTO formDTO) {
        boolean isCreateStep = StringUtils.isEmpty(formDTO.getUuid());
        Optional<Form> formOpt;
        List<FormSection> formSections;
        Form form;
        Form responseForm;

        User user = this.userRepository.fetchByUserId(userId);
        Partner partner = user.getEmployee().getPartner();

        List<FormSectionQuestionDTO> formQuestions = formDTO.getFormQuestions();

        if(isCreateStep) { // Trata-se de create
            form = formDTO.toForm();
            form.setUuid(Utilities.generateUUID());
            form.setCreatedBy(user.getUuid());
            form.setCreatedAt(DateUtils.getCurrentDate());
            form.setCode(generateFormCode(form));
            form.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        } else { // Trata-se de Edit
            formOpt = formRepository.findById(formDTO.getId());
            form = formOpt.get();
            form.setUpdatedBy(user.getUuid());
            form.setUpdatedAt(DateUtils.getCurrentDate());
        }

        form.setName(formDTO.getName());
        form.setDescription(formDTO.getDescription());
        form.setTargetPatient(formDTO.getTargetPatient());
        form.setTargetFile(formDTO.getTargetFile());
        form.setProgrammaticArea(new ProgrammaticArea(formDTO.getProgrammaticAreaDTO()));

        form.setPartner(partner);
        if (isCreateStep) { // Trata-se de create
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

            responseForm = this.formRepository.save(form);
        } else { // Trata-se de Edit
            form.setFormSections(formSectionsDTOtoFormSections(formDTO.getFormSections(), form, user));

            responseForm = this.formRepository.update(form);
        }

         return new FormDTO(responseForm);
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
    // Check if tutorUuid is valid
    if (tutorUuid == null || tutorUuid.isEmpty()) {
        throw new IllegalArgumentException("Tutor UUID cannot be null or empty");
    }

    // Retrieve the tutor by UUID
    return tutorRepository.findByUuid(tutorUuid)
        .map(tutor -> {
            // Retrieve all forms related to the tutor
            List<Form> forms = formRepository.getAllOfTutor(tutor);
            // For each form, fetch and set the related form sections
            forms.forEach(form -> form.setFormSections(formSectionService.getByForm(form)));
            return forms;
        })
        // Handle the case when the tutor is not found
        .orElseThrow(() -> new EntityNotFoundException("Tutor not found for UUID: " + tutorUuid));
}

}
