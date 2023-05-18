package mz.org.fgh.mentoring.entity.formtarget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.entity.form.Form;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity(name = "FormTarget")
@Table(name = "FORM_TARGETS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class FormTarget extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FORM_ID", nullable = false)
    private Form form;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAREER_ID", nullable = false)
    private Career career;

    @Column(name = "TARGET", nullable = false)
    private Integer target;

}
