package mz.org.fgh.mentoring.entity.formQuestion;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.question.Question;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "formQuestion")
@Table(name = "forms_questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class FormQuestion extends BaseEntity {

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn( name = "FORM_ID", nullable = false )
    private Form form;

    @NotNull
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn( name = "QUESTION_ID", nullable = false )
    private Question question;

    @NotNull
    @Column(name = "MANDATORY", nullable = false)
    private boolean mandatory;

    @Column( name ="SEQUENCE" )
    private Integer sequence;

    @NotNull
    @Column( name = "APPLICABLE", nullable = false )
    private Boolean applicable;
}
