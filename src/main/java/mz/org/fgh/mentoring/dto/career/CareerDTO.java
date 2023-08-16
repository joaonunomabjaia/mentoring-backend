package mz.org.fgh.mentoring.dto.career;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.career.Career;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerDTO {

    @JsonProperty(value = "uuid")
    private String uuid;

    @JsonProperty(value = "position")
    private String position;

    @JsonProperty(value = "careerType")
    private CareerTypeDTO careerTypeDTO;

    public CareerDTO(Career career){
        this.setUuid(career.getUuid());
        this.setPosition(career.getPosition());
        this.setCareerTypeDTO(new CareerTypeDTO(career.getCareerType()));
    }
}
