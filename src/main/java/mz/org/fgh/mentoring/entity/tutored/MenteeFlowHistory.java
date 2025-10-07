package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.MenteeFlowHistoryDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.enums.FlowHistoryProgressStatus;

import javax.persistence.*;

@Schema(name = "MenteeFlowHistory", description = "Tracks the mentee's progress across different flow histories")
@Entity
@Table(name = "mentee_flow_histories")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class MenteeFlowHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TUTORED_ID", nullable = false)
    private Tutored tutored;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FLOW_HISTORY_ID", nullable = false)
    private FlowHistory flowHistory;

    @Column(name = "PROGRESS_STATUS", nullable = false)
    private FlowHistoryProgressStatus progressStatus;
    // valores: INICIO, ISENTO, ELEGIVEL, FIM, FEITO, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RONDA_ID")
    private Ronda ronda;
    // opcional: só será preenchido caso o progresso esteja ligado a uma Ronda

    @Creator
    public MenteeFlowHistory() {}

    public MenteeFlowHistory(String uuid) {
        super(uuid);
    }

    public MenteeFlowHistory(MenteeFlowHistoryDTO dto) {
        super(dto);
        this.progressStatus = dto.getProgressStatus();
        this.tutored = new Tutored(dto.getTutoredDTO());
        this.flowHistory = new FlowHistory(dto.getFlowHistoryDTO());
        if (dto.getRondaDTO() != null) {
            this.ronda = new Ronda(dto.getRondaDTO());
        }
    }

    @Override
    public String toString() {
        return "MenteeFlowHistory{" +
                "tutored=" + tutored +
                ", flowHistory=" + flowHistory +
                ", progressStatus='" + progressStatus + '\'' +
                ", ronda=" + (ronda != null ? ronda.getUuid() : "null") +
                '}';
    }
}
