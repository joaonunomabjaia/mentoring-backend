package mz.org.fgh.mentoring.entity.form;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.programaticarea.ProgramaticArea;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity(name = "form")
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAMMATIC_AREA_ID", nullable = false)
    private ProgramaticArea programmaticArea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_TYPE_ID", nullable = false)
    private FormType formType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "form")
    private Set<FormQuestion> formQuestions;

    @NotNull
    @Column(name = "TARGET_PATIENT", nullable = false )
    private Integer targetPatient;

    @NotNull
    @Column(name = "TARGET_FILE", nullable = false)
    private Integer targetFile;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTNER_ID")
    private Partner partiner;

}
