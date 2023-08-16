package mz.org.fgh.mentoring.dto.career;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.career.CareerType;

import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
public class CareerTypeDTO implements Serializable {
    private String uuid;
    private String code;
    private String description;

    public CareerTypeDTO(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public CareerTypeDTO(String uuid, String code, String description) {
        this.uuid = uuid;
        this.code = code;
        this.description = description;
    }

    public CareerTypeDTO(CareerType careerType) {
        this.setUuid(careerType.getUuid());
        this.setCode(careerType.getCode());
        this.setDescription(careerType.getDescription());
    }

    public CareerType getCareerType() {
        return  new CareerType(this);
    }
}
