package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowHistoryDTO extends BaseEntityDTO {

    private String name;

    public FlowHistoryDTO(FlowHistory flowHistory) {
        super(flowHistory);
        this.name = flowHistory.getName();
    }

    @JsonIgnore
    public FlowHistory toEntity() {
        FlowHistory flowHistory = new FlowHistory();
        flowHistory.setId(this.getId());
        flowHistory.setUuid(this.getUuid());
        flowHistory.setCreatedAt(this.getCreatedAt());
        flowHistory.setUpdatedAt(this.getUpdatedAt());
        flowHistory.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        flowHistory.setName(this.getName());

        if (Utilities.stringHasValue(this.getLifeCycleStatus())) {
            flowHistory.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        }

        return flowHistory;
    }
}

