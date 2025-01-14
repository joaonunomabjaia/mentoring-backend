package mz.org.fgh.mentoring.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationLocation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationLocationDTO extends BaseEntityDTO {

    private String description;

    private  String code;

    public EvaluationLocationDTO(EvaluationLocation evaluationLocation) {
        super(evaluationLocation);
        this.setDescription(evaluationLocation.getDescription());
        this.setCode(evaluationLocation.getCode());
    }
}
