package mz.org.fgh.mentoring.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationTypeDTO extends BaseEntityDTO implements Serializable {

    private String description;

    private  String code;

    public EvaluationTypeDTO(EvaluationType evaluationType) {
        super(evaluationType);
        this.setDescription(evaluationType.getDescription());
        this.setCode(evaluationType.getCode());
    }

}
