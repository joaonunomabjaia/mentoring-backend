package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.dto.career.CareerDTO;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoredDTO implements Serializable {

    private String uuid;

    private String code;

    private String name;

    private String surname;

    private String phoneNumber;

    private String email;

    private int version;

    @JsonProperty(value = "career")
    private CareerDTO careerDTO;

    public TutoredDTO(Tutored tutored){
       this.setUuid(tutored.getUuid());
       this.setName(tutored.getEmployee().getName());
       this.setSurname(tutored.getEmployee().getSurname());
       this.setPhoneNumber(tutored.getEmployee().getPhoneNumber());
       this.setEmail(tutored.getEmployee().getEmail());
    }
}
