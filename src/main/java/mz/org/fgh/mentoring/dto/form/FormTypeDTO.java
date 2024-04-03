package mz.org.fgh.mentoring.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.form.FormType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormTypeDTO extends BaseEntityDTO implements Serializable {

    private Long id;

    private String uuid;

    private String description;

    private  String code;

    public FormTypeDTO(FormType formType) {
        super(formType);
        this.id = formType.getId();
        this.uuid = formType.getUuid();
        this.description = formType.getDescription();
        this.code = formType.getCode();
    }
}
