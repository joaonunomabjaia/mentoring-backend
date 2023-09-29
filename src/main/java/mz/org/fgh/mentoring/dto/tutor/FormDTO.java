package mz.org.fgh.mentoring.dto.tutor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.FormType;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDTO implements Serializable {

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

    public FormDTO(Form form) {
        this.uuid = form.getUuid();
        this.code = form.getCode();
        this.name = form.getName();
        this.description = form.getDescription();
        this.partnerDTO = new PartnerDTO(form.getPartner());
        this.programmaticAreaDTO = new ProgrammaticAreaDTO(form.getProgrammaticArea());
        this.formTypeDTO = new FormTypeDTO(form.getFormType());
        this.targetPatient = form.getTargetPatient();
        this.targetFile = form.getTargetFile();
    }
}
