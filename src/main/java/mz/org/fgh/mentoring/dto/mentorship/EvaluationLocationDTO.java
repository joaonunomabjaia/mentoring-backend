package mz.org.fgh.mentoring.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationLocation;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

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

    public EvaluationLocation toEvaluationLocation(){
        EvaluationLocation evaluationLocation = new EvaluationLocation();
        evaluationLocation.setCode(this.code);
        evaluationLocation.setDescription(this.description);
        evaluationLocation.setUuid(this.getUuid());
        evaluationLocation.setId(this.getId());
        evaluationLocation.setCreatedBy(this.getCreatedBy());
        evaluationLocation.setCreatedAt(this.getCreatedAt());
        evaluationLocation.setUpdatedAt(this.getUpdatedAt());
        evaluationLocation.setUpdatedBy(this.getUpdatedBy());

        return evaluationLocation;
    }
}
