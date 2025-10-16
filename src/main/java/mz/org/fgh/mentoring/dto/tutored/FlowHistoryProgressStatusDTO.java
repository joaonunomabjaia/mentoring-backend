package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowHistoryProgressStatusDTO extends BaseEntityDTO {

    private String name;
    private String description;

    public FlowHistoryProgressStatusDTO(FlowHistoryProgressStatus entity) {
        super(entity);
        this.name = entity.getName();
        this.description = entity.getDescription();
    }

    @JsonIgnore
    public FlowHistoryProgressStatus toEntity() {
        FlowHistoryProgressStatus entity = new FlowHistoryProgressStatus();
        entity.setId(this.getId());
        entity.setUuid(this.getUuid());
        entity.setCreatedAt(this.getCreatedAt());
        entity.setUpdatedAt(this.getUpdatedAt());

        if (Utilities.stringHasValue(this.getLifeCycleStatus())) {
            entity.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        }

        entity.setName(this.name);
        entity.setDescription(this.getDescription());
        return entity;
    }
}
