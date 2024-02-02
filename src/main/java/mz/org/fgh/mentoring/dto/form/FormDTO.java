package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDTO extends BaseEntityDTO implements Serializable {

    private String uuid;

    private String code;

    private String name;

    private String description;

    @JsonProperty(value = "partner")
    private PartnerDTO partnerDTO;

    @JsonProperty(value = "programmaticArea")
    private ProgrammaticAreaDTO programmaticAreaDTO;

    @JsonProperty(value = "formType")
    private FormTypeDTO formTypeDTO;

    private Integer targetPatient;

    private Integer targetFile;

    private String lifeCycleStatus;

    public FormDTO(Form form) {
        super(form);
        this.code = form.getCode();
        this.name = form.getName();
        this.description = form.getDescription();
        if(form.getPartner()!=null) {
            this.partnerDTO = new PartnerDTO(form.getPartner());
        }
        if(form.getProgrammaticArea()!=null) {
            this.programmaticAreaDTO = new ProgrammaticAreaDTO(form.getProgrammaticArea());
        }
        if(form.getFormType()!=null) {
            this.formTypeDTO = new FormTypeDTO(form.getFormType());
        }
        this.targetPatient = form.getTargetPatient();
        this.targetFile = form.getTargetFile();
        this.lifeCycleStatus = form.getLifeCycleStatus().name();
    }
}
