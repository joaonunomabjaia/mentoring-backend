package mz.org.fgh.mentoring.dto.tutor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.dto.career.CareerDTO;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.UserDTO;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO implements Serializable {
    private String uuid;
    private String name;

    private String sunname;

    @JsonProperty(value = "careerDTO")
    private CareerDTO careerDTO;

    private String phoneNumber;

    private String code;

    private String email;

    private boolean isUser;

    @JsonProperty(value = "partnerDTO")
    private PartnerDTO partnerDTO;

    @JsonProperty(value = "userDTO")
    private UserDTO userDTO;

    public TutorDTO(Tutor tutor) {
        this.setUuid(tutor.getUuid());
        this.setName(tutor.getEmployee().getName());
        this.setSunname(tutor.getEmployee().getSurname());
        this.setPhoneNumber(tutor.getEmployee().getPhoneNumber());
        this.setEmail(tutor.getEmployee().getEmail());
        this.setPartnerDTO(new PartnerDTO(tutor.getEmployee().getPartner()));

    }
}