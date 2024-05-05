package mz.org.fgh.mentoring.dto.tutor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.career.CareerDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.dto.user.UserDTO;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO extends BaseEntityDTO {
    private EmployeeDTO employeeDTO;

    public TutorDTO(Tutor tutor) {
        super(tutor);
        this.setEmployeeDTO(new EmployeeDTO(tutor.getEmployee()));
    }

    public Tutor getTutor() {
        Tutor tutor = new Tutor();
        tutor.setId(this.getId());
        tutor.setUuid(this.getUuid());
        if(this.getEmployeeDTO()!=null) {
            tutor.setEmployee(this.getEmployeeDTO().getEmployee());
        }
        return tutor;
    }
}