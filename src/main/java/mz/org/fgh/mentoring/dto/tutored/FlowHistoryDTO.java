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
@Builder
public class FlowHistoryDTO extends BaseEntityDTO {

    private String name;
    private String description;
    private FlowHistoryProgressStatusDTO progressStatus;

    public FlowHistoryDTO(FlowHistory entity) {
        super(entity);
        this.name = entity.getName();
        this.description = entity.getDescription();
    }

    @JsonIgnore
    public FlowHistory toEntity() {
        FlowHistory entity = new FlowHistory();
        entity.setId(this.getId());
        entity.setUuid(this.getUuid());
        entity.setCreatedAt(this.getCreatedAt());
        entity.setUpdatedAt(this.getUpdatedAt());
        entity.setName(this.getName());
        entity.setDescription(this.getDescription());

        if (Utilities.stringHasValue(this.getLifeCycleStatus())) {
            entity.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        }

        return entity;
    }
}
