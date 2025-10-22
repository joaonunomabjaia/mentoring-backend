package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.FlowHistoryProgressStatusDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Schema(name = "FlowHistoryProgressStatus", description = "Represents the progress status of a flow history (e.g. INICIO, ISENTO, ELEGIVEL, FIM, FEITO)")
@Entity
@Table(name = "flow_history_progress_statuses")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Getter
@Setter
public class FlowHistoryProgressStatus extends BaseEntity {

    @Column(name = "NAME", nullable = false, unique = true, length = 20)
    private String name; // Ex.: INICIO, ISENTO, ELEGIVEL, FIM, FEITO

    @Column(name = "DESCRIPTION", length = 100)
    private String description; // Ex.: "Sessão inicial", "Isento de mentoria", etc.

    @Creator
    public FlowHistoryProgressStatus() {}

    public FlowHistoryProgressStatus(String uuid) {
        super(uuid);
    }

    public FlowHistoryProgressStatus(FlowHistoryProgressStatusDTO dto) {
        super(dto);
        this.name = dto.getName();
        this.description = dto.getDescription();
    }

    @Override
    public String toString() {
        return "FlowHistoryProgressStatus{" +
                "code='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
