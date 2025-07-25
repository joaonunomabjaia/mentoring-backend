package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

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

    @Setter
    @Getter
    private Double zeroEvaluationScore;

    public TutoredDTO(Tutored tutored) {
        super(tutored);
        this.zeroEvaluationDone = tutored.getZeroEvaluationScore() != null;
        this.zeroEvaluationScore = tutored.getZeroEvaluationScore();
        if(tutored.getEmployee()!=null) this.setEmployeeDTO(new EmployeeDTO(tutored.getEmployee()));

    }

    @JsonIgnore
    public Tutored toEntity() {
        Tutored tutored = new Tutored();
        tutored.setId(this.getId());
        tutored.setUuid(this.getUuid());
        tutored.setCreatedAt(this.getCreatedAt());
        tutored.setUpdatedAt(this.getUpdatedAt());
        tutored.setZeroEvaluationScore(this.getZeroEvaluationScore());
        tutored.setZeroEvaluationDone(this.isZeroEvaluationDone());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) tutored.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        if(this.getEmployeeDTO()!=null) {
            tutored.setEmployee(this.getEmployeeDTO().toEntity());
        }
        return tutored;
    }

}
