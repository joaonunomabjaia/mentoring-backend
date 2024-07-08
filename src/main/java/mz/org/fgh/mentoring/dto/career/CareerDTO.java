package mz.org.fgh.mentoring.dto.career;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.career.Career;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerDTO extends BaseEntityDTO {

    @JsonProperty(value = "uuid")
    private String uuid;

    @JsonProperty(value = "position")
    private String position;

    @JsonProperty(value = "careerType")
    private CareerTypeDTO careerTypeDTO;

    public CareerDTO(Career career){
        super(career);
        this.setPosition(career.getPosition());
        this.setCareerTypeDTO(new CareerTypeDTO(career.getCareerType()));
    }
}
