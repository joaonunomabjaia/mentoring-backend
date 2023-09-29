package mz.org.fgh.mentoring.dto.tutor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.form.FormType;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormTypeDTO implements Serializable {

    private String description;

    private  String code;

    public FormTypeDTO(FormType formType) {
        this.description = formType.getDescription();
        this.code = formType.getCode();
    }
}
