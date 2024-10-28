package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.FormSection;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class FormDTO extends BaseEntityDTO {

    @NotEmpty(message = "Code cannot be empty")
    @Size(max = 50, message = "Code cannot be longer than 50 characters")
    private String code;

    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 250, message = "Name cannot be longer than 150 characters")
    private String name;

    @Size(max = 455, message = "Description cannot be longer than 255 characters")
    private String description;

    @JsonProperty(value = "partner")
    private PartnerDTO partnerDTO;

    @JsonProperty(value = "programmaticAreaDTO")
    private ProgrammaticAreaDTO programmaticAreaDTO;

    @JsonProperty(value = "formQuestions")
    private List<FormSectionQuestionDTO> formQuestions = new ArrayList<>();

    @JsonProperty(value = "formSections")
    private List<FormSectionDTO> formSections = new ArrayList<>();

    private Integer targetPatient;

    private Integer targetFile;

    private Date createdAt;

    private String createdBy;


    @Creator
    public FormDTO() {
        super();
    }

    public FormDTO(Form form) {
        super(form);
        this.code = form.getCode();
        this.name = form.getName();
        this.description = form.getDescription();
        this.createdAt = form.getCreatedAt();
        this.createdBy = form.getCreatedBy();
        this.targetPatient = form.getTargetPatient();
        this.targetFile = form.getTargetFile();
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) form.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));

        if (form.getPartner() != null) {
            this.partnerDTO = new PartnerDTO(form.getPartner());
        }
        if (form.getProgrammaticArea() != null) {
            this.programmaticAreaDTO = new ProgrammaticAreaDTO(form.getProgrammaticArea());
        }
        if (Utilities.listHasElements(form.getFormSections())) {
            this.formSections = new ArrayList<>();
            for (FormSection formSection : form.getFormSections()) {
                formSection.setForm(form);
                this.formSections.add(new FormSectionDTO(formSection));
            }
        }
    }

    public Form toForm() {
        Form form = new Form();
        form.setUuid(this.getUuid());
        form.setId(this.getId());
        form.setCreatedAt(this.getCreatedAt());
        form.setUpdatedAt(this.getUpdatedAt());
        form.setDescription(this.getDescription());
        form.setName(this.getName());
        form.setCode(this.getCode());
        form.setTargetFile(this.getTargetFile());
        form.setTargetPatient(this.getTargetPatient());

        if (Utilities.stringHasValue(this.getLifeCycleStatus())) {
            form.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        }
        if (this.getPartnerDTO() != null) {
            form.setPartner(new Partner(this.getPartnerDTO()));
        }
        if (this.getProgrammaticAreaDTO() != null) {
            form.setProgrammaticArea(new ProgrammaticArea(this.getProgrammaticAreaDTO()));
        }
        if (Utilities.listHasElements(this.formSections)) {
            form.setFormSections(new ArrayList<>());
            for (FormSectionDTO section : this.formSections) {
                FormSection formSection = new FormSection(section);
                formSection.setFormSectionQuestions(new ArrayList<>());
                if (Utilities.listHasElements(section.getFormQuestionDTOList())) {
                    for (FormSectionQuestionDTO question : section.getFormQuestionDTOList()) {
                        formSection.getFormSectionQuestions().add(new FormSectionQuestion(question));
                    }
                }
                form.getFormSections().add(formSection);
            }
        }
        return form;
    }
}
