package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowHistoryProgressStatusDTO extends BaseEntityDTO {

    /** Canonical enum code (e.g., "AGUARDA_INICIO") */
    private String code;

    /** Human-readable label (e.g., "AGUARDA INICIO") */
    private String name;

    private String description;

    public FlowHistoryProgressStatusDTO(FlowHistoryProgressStatus entity) {
        super(entity);
        this.code = entity.getCode();
        this.name = entity.getName();
        this.description = entity.getDescription();
    }

    @JsonIgnore
    public FlowHistoryProgressStatus toEntity() {
        FlowHistoryProgressStatus e = new FlowHistoryProgressStatus();
        e.setId(getId());
        e.setUuid(getUuid());
        e.setCreatedAt(getCreatedAt());
        e.setUpdatedAt(getUpdatedAt());

        if (Utilities.stringHasValue(getLifeCycleStatus())) {
            e.setLifeCycleStatus(LifeCycleStatus.valueOf(getLifeCycleStatus()));
        }

        e.setCode(this.code);
        e.setName(this.name);
        e.setDescription(this.description);
        return e;
    }
}
