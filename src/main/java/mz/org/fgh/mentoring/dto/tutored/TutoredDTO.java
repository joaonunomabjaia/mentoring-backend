package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.career.CareerDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoredDTO extends BaseEntityDTO {
    private EmployeeDTO employeeDTO;

    public TutoredDTO(Tutored tutored) {
        super(tutored);
       this.setEmployeeDTO(new EmployeeDTO(tutored.getEmployee()));

    }
}
