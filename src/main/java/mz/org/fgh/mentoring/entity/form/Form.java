package mz.org.fgh.mentoring.entity.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.form.FormSectionDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.util.Utilities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Form")
@Table(name = "forms")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@Introspected
public class Form extends BaseEntity {

    @NotEmpty
    @Column(name = "CODE", nullable = false, length = 50, unique = true)
    private String code;

    @NotEmpty
    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @NotEmpty
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @ToString.Exclude
    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROGRAMMATIC_AREA_ID", nullable = false)
    private ProgrammaticArea programmaticArea;



    @NotNull
    @Column(name = "TARGET_PATIENT", nullable = false)
    private Integer targetPatient;

    @NotNull
    @Column(name = "TARGET_FILE", nullable = false)
    private Integer targetFile;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTNER_ID", nullable = false)
    private Partner partner;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "form", cascade = CascadeType.ALL)
    private List<Answer> answers;

    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "form", cascade = CascadeType.ALL)
    private List<FormSection> formSections;

    @Creator
    public Form(){}
    // Constructor to initialize entity from FormDTO
    public Form(FormDTO formDTO) {
        super(formDTO);
        this.setCode(formDTO.getCode());
        this.setDescription(formDTO.getDescription());
        this.setName(formDTO.getName());
        this.setTargetFile(formDTO.getTargetFile());
        this.setTargetPatient(formDTO.getTargetPatient());
        if (formDTO.getPartnerDTO() != null) {
            this.setPartner(new Partner(formDTO.getPartnerDTO()));
        }
        if (formDTO.getProgrammaticAreaDTO() != null) {
            this.setProgrammaticArea(new ProgrammaticArea(formDTO.getProgrammaticAreaDTO()));
        }

        if (Utilities.listHasElements(formDTO.getFormSections())) {
            this.setFormSections(new ArrayList<>());
            for (FormSectionDTO section : formDTO.getFormSections()) {
                this.getFormSections().add(new FormSection(section));
            }
        }
    }

    public Form(Long formId) {
        this.setId(formId);
    }

    @Override
    public String toString() {
        return "Form{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", targetPatient=" + targetPatient +
                ", targetFile=" + targetFile +
                '}';
    }
}
