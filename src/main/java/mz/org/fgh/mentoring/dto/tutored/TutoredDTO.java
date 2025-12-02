package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TutoredDTO")
public class TutoredDTO extends BaseEntityDTO {

    private EmployeeDTO employeeDTO;
    private Double zeroEvaluationScore;
    private boolean zeroEvaluationDone;

    /** Lista com 1 ou 2 registros (primeiro e último MenteeFlowHistory) */
    private List<FlowHistoryMenteeAuxDTO> flowHistoryMenteeAuxDTO = new ArrayList<>();

    public TutoredDTO(Tutored tutored) {
        super(tutored);

        this.zeroEvaluationScore = tutored.getZeroEvaluationScore();
        this.zeroEvaluationDone = tutored.isZeroEvaluationDone();

        if (tutored.getEmployee() != null) {
            this.employeeDTO = new EmployeeDTO(tutored.getEmployee());
        }

        // ⚙️ Verifica se há históricos de mentoria
        if (tutored.getMenteeFlowHistories() != null
                && Hibernate.isInitialized(tutored.getMenteeFlowHistories())
                && !tutored.getMenteeFlowHistories().isEmpty()) {

            List<MenteeFlowHistory> sorted = tutored.getMenteeFlowHistories().stream()
                    .filter(m -> m.getSequenceNumber() != null)
                    .sorted(Comparator.comparing(MenteeFlowHistory::getSequenceNumber))
                    .toList();

            MenteeFlowHistory first = sorted.get(0);
            MenteeFlowHistory last = sorted.get(sorted.size() - 1);

            // Sempre adiciona o primeiro
            this.flowHistoryMenteeAuxDTO.add(buildFlowHistoryAux(first));

            // Se houver mais de um, adiciona também o último
            if (sorted.size() > 1 && !first.equals(last)) {
                this.flowHistoryMenteeAuxDTO.add(buildFlowHistoryAux(last));
            }
        }
    }

    private FlowHistoryMenteeAuxDTO buildFlowHistoryAux(MenteeFlowHistory history) {
        return FlowHistoryMenteeAuxDTO.builder()
                .estagio(history.getFlowHistory() != null ? history.getFlowHistory().getCode() : null)
                .estado(history.getProgressStatus() != null ? history.getProgressStatus().getCode() : null)
                .classificacao(history.getClassification())
                .seq(history.getSequenceNumber())
                .build();
    }

    @JsonIgnore
    public Tutored toEntity() {
        return new Tutored(this);
    }

}
