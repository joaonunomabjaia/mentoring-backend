package mz.org.fgh.mentoring.entity.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Schema(name = "FormType", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "FormType")
@Table(name = "FORM_TYPE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class FormType extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;
}
