package mz.org.fgh.mentoring.entity.formQuestion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.question.Question;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Comparator;

@Schema(name = "FormQuestion", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "FormQuestion")
@Table(name = "forms_questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class FormQuestion extends BaseEntity {

    @ToString.Exclude
    @NotNull
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn( name = "FORM_ID", nullable = false )
    private Form form;

    @ToString.Exclude
    @JsonIgnore
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
