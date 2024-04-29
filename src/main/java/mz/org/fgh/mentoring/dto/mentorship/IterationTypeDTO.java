package mz.org.fgh.mentoring.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IterationTypeDTO extends BaseEntityDTO implements Serializable {

    private String description;

    private  String code;

    public IterationTypeDTO(IterationType iterationType) {
        super(iterationType);
        this.description = iterationType.getDescription();
        this.code = iterationType.getCode();
    }

    public IterationType toIterationType() {
        IterationType iterationType = new IterationType();
        iterationType.setCode(this.getCode());
        iterationType.setId(this.getId());
        iterationType.setDescription(this.getDescription());
        iterationType.setUuid(this.getUuid());
        iterationType.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        return iterationType;
    }
}
