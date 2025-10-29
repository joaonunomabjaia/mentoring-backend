package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "TutoredDTO")
public class TutoredDTO extends BaseEntityDTO {

    private EmployeeDTO employeeDTO;
    private Double zeroEvaluationScore;
    private boolean zeroEvaluationDone;

    /** Carries ENUM CODES (name()), not labels */
    private FlowHistoryMenteeAuxDTO flowHistoryMenteeAuxDTO;

    public TutoredDTO(Tutored tutored) {
        super(tutored);
        this.zeroEvaluationScore = tutored.getZeroEvaluationScore();
        this.zeroEvaluationDone  = tutored.isZeroEvaluationDone();

        if (tutored.getEmployee() != null) {
            this.employeeDTO = new EmployeeDTO(tutored.getEmployee());
        }

        tutored.getLastMenteeFlowHistory().ifPresent(last -> {
            // Entities must expose code (enum name) via getCode()
            String estagioCode = last.getFlowHistory().getCode();
            String estadoCode  = last.getProgressStatus().getCode();

            this.flowHistoryMenteeAuxDTO = FlowHistoryMenteeAuxDTO.builder()
                    .estagio(estagioCode)       // e.g., "RONDA_CICLO"
                    .estado(estadoCode)         // e.g., "AGUARDA_INICIO"
                    .classificacao(last.getClassification())
                    .build();
        });
    }

    @JsonIgnore
    public Tutored toEntity() {
        Tutored t = new Tutored();
        t.setId(getId());
        t.setUuid(getUuid());
        t.setCreatedAt(getCreatedAt());
        t.setUpdatedAt(getUpdatedAt());
        t.setZeroEvaluationScore(getZeroEvaluationScore());

        if (Utilities.stringHasValue(getLifeCycleStatus())) {
            t.setLifeCycleStatus(LifeCycleStatus.valueOf(getLifeCycleStatus()));
        }
        if (employeeDTO != null) {
            t.setEmployee(employeeDTO.toEntity());
        }
        // FlowHistory for the mentee is managed by service layer (not here)
        return t;
    }
}
