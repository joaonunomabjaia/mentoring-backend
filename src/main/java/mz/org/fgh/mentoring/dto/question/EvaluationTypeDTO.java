package mz.org.fgh.mentoring.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.EvaluationType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationTypeDTO extends BaseEntityDTO implements Serializable {

    private String description;

    private  String code;

    public EvaluationTypeDTO(EvaluationType evaluationType) {
        super(evaluationType);
        this.description = evaluationType.getDescription();
        this.code = evaluationType.getCode();
    }

    public EvaluationType toEvaluationType() {
        EvaluationType evaluationType = new EvaluationType();
        evaluationType.setCode(this.getCode());
        evaluationType.setId(this.getId());
        evaluationType.setDescription(this.getDescription());
        evaluationType.setUuid(this.getUuid());
        evaluationType.setLifeCycleStatus(this.getLifeCycleStatus());
        return evaluationType;
    }
}
