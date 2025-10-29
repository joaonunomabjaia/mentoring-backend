package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutoredDTO extends BaseEntityDTO {

    private EmployeeDTO employeeDTO;
    private Double zeroEvaluationScore;
    private boolean zeroEvaluationDone;
    //NOVO
    private FlowHistoryMenteeAuxDTO flowHistoryMenteeAuxDTO;

    public TutoredDTO(Tutored tutored) {
        super(tutored);
        this.zeroEvaluationScore = tutored.getZeroEvaluationScore();
        this.zeroEvaluationDone = tutored.isZeroEvaluationDone(); // usa o getter inteligente
        if (tutored.getEmployee() != null) {
            this.setEmployeeDTO(new EmployeeDTO(tutored.getEmployee()));
        }

        tutored.getLastMenteeFlowHistory().ifPresent(last -> {
            this.flowHistoryMenteeAuxDTO = FlowHistoryMenteeAuxDTO.builder()
                    .estagio(last.getFlowHistory().getName())
                    .estado(last.getProgressStatus().getName())
                    .classificacao(last.getClassification())
                    .build();
        });
    }


    @JsonIgnore
    public Tutored toEntity() {
        Tutored tutored = new Tutored();
        tutored.setId(this.getId());
        tutored.setUuid(this.getUuid());
        tutored.setCreatedAt(this.getCreatedAt());
        tutored.setUpdatedAt(this.getUpdatedAt());
        tutored.setZeroEvaluationScore(this.getZeroEvaluationScore());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) {
            tutored.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        }
        if (this.getEmployeeDTO() != null) {
            tutored.setEmployee(this.getEmployeeDTO().toEntity());
        }
        return tutored;
    }
}
