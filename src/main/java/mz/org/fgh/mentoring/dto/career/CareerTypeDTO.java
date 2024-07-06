package mz.org.fgh.mentoring.dto.career;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.career.CareerType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
public class CareerTypeDTO extends BaseEntityDTO {
    private String code;
    private String description;

    public CareerTypeDTO(CareerType careerType) {
        super(careerType);
        this.setCode(careerType.getCode());
        this.setDescription(careerType.getDescription());
    }
}
