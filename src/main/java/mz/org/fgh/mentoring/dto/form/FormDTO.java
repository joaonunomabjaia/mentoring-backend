package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDTO extends BaseEntityDTO implements Serializable {

    private String code;

    private String name;

    private String description;

    @JsonProperty(value = "partner")
    private PartnerDTO partnerDTO;

    @JsonProperty(value = "programmaticArea")
    private ProgrammaticAreaDTO programmaticAreaDTO;

    @JsonProperty(value = "formQuestions")
    private List<FormQuestionDTO> formQuestions = new ArrayList<FormQuestionDTO>();

    private Integer targetPatient;

    private Integer targetFile;

    private Date createdAt;

    private String createdBy;

    public FormDTO(Form form) {
        super(form);
        this.code = form.getCode();
        this.name = form.getName();
        this.description = form.getDescription();
        this.createdAt = form.getCreatedAt();
        this.createdBy = form.getCreatedBy();
        if(form.getPartner()!=null) {
            this.partnerDTO = new PartnerDTO(form.getPartner());
        }
        if(form.getProgrammaticArea()!=null) {
            this.programmaticAreaDTO = new ProgrammaticAreaDTO(form.getProgrammaticArea());
        }
        if(form.getFormQuestions()!=null && !form.getFormQuestions().isEmpty()) {
            List<FormQuestion> formQuestionList = form.getFormQuestions();
            for (FormQuestion formQuestion: formQuestionList) {
                FormQuestionDTO formQuestionDTO = new FormQuestionDTO(formQuestion);
                formQuestions.add(formQuestionDTO);
            }
        }
        this.targetPatient = form.getTargetPatient();
        this.targetFile = form.getTargetFile();
    }

    public Form toForm() {
        Form form = new Form();
        form.setId(this.getId());
        form.setUuid(this.getUuid());
        form.setCode(this.getCode());
        form.setName(this.getName());
        form.setDescription(this.getDescription());
        form.setTargetFile(this.getTargetFile());
        form.setTargetPatient(this.getTargetPatient());
        form.setCreatedAt(this.getCreatedAt());
        form.setCreatedBy(this.getCreatedBy());
        if(this.getProgrammaticAreaDTO()!=null) {
            form.setProgrammaticArea(this.getProgrammaticAreaDTO().toProgrammaticArea());
        }
        return form;
    }
}
