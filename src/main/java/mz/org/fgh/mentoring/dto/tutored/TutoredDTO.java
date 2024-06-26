package mz.org.fgh.mentoring.dto.tutored;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

/**
 * @author Jose Julai Ritsure
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoredDTO extends BaseEntityDTO {
    private EmployeeDTO employeeDTO;

    @Setter
    @Getter
    private boolean zeroEvaluationDone;

    public TutoredDTO(Tutored tutored) {
        super(tutored);
       this.setEmployeeDTO(new EmployeeDTO(tutored.getEmployee()));

    }

    public Tutored getMentee() {
        Tutored tutored = new Tutored();
        tutored.setId(this.getId());
        tutored.setUuid(this.getUuid());
        if(this.getEmployeeDTO()!=null) {
            tutored.setEmployee(this.getEmployeeDTO().getEmployee());
        }
        return tutored;
    }

}
