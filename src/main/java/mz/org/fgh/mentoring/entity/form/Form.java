package mz.org.fgh.mentoring.entity.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity(name = "Form")
@Table(name = "forms")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Form extends BaseEntity {

    @NotEmpty
    @Column(name = "CODE", nullable = false, length = 50 )
    private String code;

    @NotEmpty
    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @NotEmpty
    @Column(name = "DESCRIPTION")
    private String description;

    @ToString.Exclude
    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROGRAMMATIC_AREA_ID", nullable = false)
    private ProgrammaticArea programmaticArea;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "form")
    private List<FormQuestion> formQuestions;

    @NotNull
    @Column(name = "TARGET_PATIENT", nullable = false )
    private Integer targetPatient;

    @NotNull
    @Column(name = "TARGET_FILE", nullable = false)
    private Integer targetFile;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTNER_ID")
    private Partner partner;

}
