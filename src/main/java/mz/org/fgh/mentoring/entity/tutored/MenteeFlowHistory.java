package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.FlowHistoryMenteeAuxDTO;
import mz.org.fgh.mentoring.dto.tutored.MenteeFlowHistoryDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROGRESS_STATUS_ID", nullable = false)
    private FlowHistoryProgressStatus progressStatus;
    // Valores: dinâmicos, provenientes da tabela flow_history_progress_statuses

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RONDA_ID")
    private Ronda ronda;
    // Opcional: só será preenchido se o progresso estiver ligado a uma Ronda

    @Column(name = "CLASSIFICATION")
    private double classification;

    @Column(name = "SEQUENCE_NUMBER")
    private Integer sequenceNumber;


    @Creator
    public MenteeFlowHistory() {}

    public MenteeFlowHistory(String uuid) {
        super(uuid);
    }

    public MenteeFlowHistory(MenteeFlowHistoryDTO dto) {
        super(dto); // seta UUID da base
        this.tutored = dto.toEntity().getTutored();
        this.flowHistory = dto.toEntity().getFlowHistory();
        this.progressStatus = dto.toEntity().getProgressStatus();
        this.ronda = dto.toEntity().getRonda();
        this.classification = dto.toEntity().getClassification();
    }

    public MenteeFlowHistory(FlowHistoryMenteeAuxDTO flowHistoryMenteeAuxDTO){
        this.flowHistory = new FlowHistory();
        this.flowHistory.setCode(flowHistoryMenteeAuxDTO.estagio());
        this.progressStatus = new FlowHistoryProgressStatus();
        this.progressStatus.setCode(flowHistoryMenteeAuxDTO.estado());
        if (flowHistoryMenteeAuxDTO.classificacao() == null) {
            this.classification = 0;
        } else {
            this.classification = flowHistoryMenteeAuxDTO.classificacao();
        }
    }

    @Override
    public String toString() {
        return "MenteeFlowHistory{" +
                "tutored=" + tutored +
                ", flowHistory=" + flowHistory +
                ", progressStatus=" + (progressStatus != null ? progressStatus.getName() : "null") +
                ", classificacao=" + classification +
                ", ronda=" + (ronda != null ? ronda.getUuid() : "null") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MenteeFlowHistory that = (MenteeFlowHistory) o;
        return Objects.equals(flowHistory, that.flowHistory) && Objects.equals(progressStatus, that.progressStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), flowHistory, progressStatus);
    }
}
