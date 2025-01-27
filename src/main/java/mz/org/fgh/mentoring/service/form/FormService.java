package mz.org.fgh.mentoring.service.form;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.form.FormSectionDTO;
import mz.org.fgh.mentoring.dto.form.FormSectionQuestionDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.FormSection;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationLocation;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.ResponseType;
import mz.org.fgh.mentoring.entity.question.Section;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.form.FormRepository;
import mz.org.fgh.mentoring.repository.form.FormSectionQuestionRepository;
import mz.org.fgh.mentoring.repository.form.FormSectionRepository;
import mz.org.fgh.mentoring.repository.mentorship.EvaluationLocationRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.partner.PartnerService;
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
    @Inject
    private PartnerService partnerService;
    @Inject
    private final EvaluationLocationRepository evaluationLocationRepository;


    public FormService(UserRepository userRepository, FormRepository formRepository, FormSectionQuestionRepository formQuestionRepository, EvaluationLocationRepository evaluationLocationRepository) {
        this.userRepository = userRepository;
        this.formRepository = formRepository;
        this.formQuestionRepository = formQuestionRepository;
        this.evaluationLocationRepository = evaluationLocationRepository;
    }

    public Page<FormDTO> findAll(Pageable pageable) {
        // Fetch paginated forms from the repository
        Page<Form> formPage = this.formRepository.findAllWithFormSections(pageable);

        // Convert the Page<Form> to Page<FormDTO> by mapping the Form entities to DTOs
        return formPage.map(this::newFormDTO);
    }

    public FormDTO newFormDTO(Form form) {
        return new FormDTO(form);
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
        FormDTO formDTO = new FormDTO(form);
        for (FormSectionDTO formSectionDTO : formDTO.getFormSections()) {
            if (formSectionService.formSectionInUse(new FormSection(formSectionDTO))){
                formSectionDTO.setInUse(true);
            } else {
                formSectionDTO.setInUse(false);
            }
        }

        return formDTO;
    }

    public Form updateLifeCycleStatus(Form form, Long userId) {
        User user = this.userRepository.fetchByUserId(userId);
        Optional<Form> formOpt = this.formRepository.findByUuid(form.getUuid());

        if (formOpt.isPresent()) {
            Form existingForm = formOpt.get();

            // Check if all form sections have associated form section questions
            boolean allSectionsValid = existingForm.getFormSections().stream()
                    .allMatch(section -> section.getFormSectionQuestions() != null
                            && !section.getFormSectionQuestions().isEmpty());

            if (allSectionsValid) {
                // Update the lifecycle status to ACTIVE
                existingForm.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                existingForm.setUpdatedBy(user.getUuid());
                existingForm.setUpdatedAt(DateUtils.getCurrentDate());
                this.formRepository.update(existingForm);

                return existingForm;
            } else {
                // Emit a message for the user
                throw new IllegalStateException(
                        "A tabela não pode ser ativada. Todas as seções do devem ter competências associadas."
                );
            }
        }

        throw new EntityNotFoundException("Tabela não encontrada com o UUID: " + form.getUuid());
    }


    private List<FormSection> formSectionsDTOtoFormSections(List<FormSectionDTO> formSectionDTOS, Form form, User user){
        List<FormSection> formSections = new ArrayList<>();

        for (FormSectionDTO formSectionDTO : formSectionDTOS) {// Novas formSections e antigas
            FormSection formSection = new FormSection(formSectionDTO);
            formSection.setForm(form);

            if (!existsOnDB(formSection.getSection())) {
                formSection.getSection().setCreatedBy(user.getUuid());
                formSection.getSection().setCreatedAt(DateUtils.getCurrentDate());
                formSection.getSection().setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                formSection.setSection(sectionService.save(formSection.getSection())); // Explicitly save the `Section`
            }

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
        Form form;
        Form responseForm;

        User user = this.userRepository.fetchByUserId(userId);

        if (isCreateStep) { // Create operation
            form = formDTO.toForm();
            form.setUuid(Utilities.generateUUID());
            form.setCreatedBy(user.getUuid());
            form.setCreatedAt(DateUtils.getCurrentDate());
            form.setCode(generateFormCode(form));
            determineLifeCycleStatus(form);
            form.setEvaluationLocation(evaluationLocationRepository.findByUuid(formDTO.getEvaluationLocationDTO().getUuid()).get());
        } else { // Edit operation
            formOpt = formRepository.findById(formDTO.getId());
            form = formOpt.get();
            form.setUpdatedBy(user.getUuid());
            form.setUpdatedAt(DateUtils.getCurrentDate());
            if (form.getLifeCycleStatus().equals(LifeCycleStatus.INACTIVE)) {
                determineLifeCycleStatus(form);
            }
        }

        form.setName(formDTO.getName());
        form.setDescription(formDTO.getDescription());
        form.setTargetPatient(formDTO.getTargetPatient());
        form.setTargetFile(formDTO.getTargetFile());
        form.setProgrammaticArea(new ProgrammaticArea(formDTO.getProgrammaticAreaDTO()));
        form.setPartner(partnerService.getMISAU());

        if (isCreateStep) {
            for (FormSection fs : form.getFormSections()) {
                // Load or persist the `Section` if necessary
                Section section = fs.getSection();
                fs.setForm(form);
                if (!existsOnDB(section)) {
                    section.setCreatedBy(user.getUuid());
                    section.setCreatedAt(DateUtils.getCurrentDate());
                    section.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                    fs.setSection(sectionService.save(section)); // Explicitly save the `Section`
                }
                fs.setSection(section);// Associate `Form` with `FormSection`
                fs.setCreatedBy(user.getUuid());
                fs.setCreatedAt(DateUtils.getCurrentDate());
                fs.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

                if (Utilities.listHasElements(fs.getFormSectionQuestions())) {
                    for (FormSectionQuestion fq : fs.getFormSectionQuestions()) {
                        fq.setFormSection(fs); // Associate `FormSection` with `FormSectionQuestion`
                        fq.setEvaluationLocation(evaluationLocationRepository.findByUuid(formDTO.getEvaluationLocationDTO().getUuid()).get());
                        fq.setCreatedBy(user.getUuid());
                        fq.setCreatedAt(DateUtils.getCurrentDate());
                        fq.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                    }
                }
            }
            responseForm = this.formRepository.save(form); // Persist the `Form` first

        } else { // Edit operation
            form.setFormSections(formSectionsDTOtoFormSections(formDTO.getFormSections(), form, user));
            responseForm = this.formRepository.update(form);
        }

        return new FormDTO(responseForm);
    }




    private void determineLifeCycleStatus(Form form) {
        if (allSectionsHaveQuestions(form)) {
            form.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        } else {
            form.setLifeCycleStatus(LifeCycleStatus.INACTIVE);
        }
    }

    private boolean allSectionsHaveQuestions(Form form) {
        for (FormSection formSection : form.getFormSections()) {
            if (!Utilities.listHasElements(formSection.getFormSectionQuestions())) return false;
        }
        return true;
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
