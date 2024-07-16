package mz.org.fgh.mentoring.dto.ronda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.ronda.RondaType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RondaTypeDTO extends BaseEntityDTO {

    private String code;

    private String description;

    public RondaTypeDTO(RondaType rondaType) {
        super(rondaType);
        this.setCode(rondaType.getCode());
        this.setDescription(rondaType.getDescription());
    }

    public RondaType getRondaType() {
        RondaType rondaType = new RondaType();
        rondaType.setId(this.getId());
        rondaType.setUuid(this.getUuid());
        rondaType.setCode(this.getCode());
        rondaType.setDescription(this.getDescription());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) rondaType.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        return rondaType;
    }
}
