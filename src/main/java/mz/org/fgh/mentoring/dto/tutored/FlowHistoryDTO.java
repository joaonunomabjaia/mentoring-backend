package mz.org.fgh.mentoring.dto.tutored;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowHistoryDTO extends BaseEntityDTO {

    /** Canonical enum code (e.g., "RONDA_CICLO") */
    private String code;

    /** Human-readable label (e.g., "RONDA / CICLO ATC") */
    private String name;

    private String description;

    /** Optional status detail to bundle with the stage when needed */
    private FlowHistoryProgressStatusDTO progressStatus;

    public FlowHistoryDTO(FlowHistory entity) {
        super(entity);
        this.code = entity.getCode();
        this.name = entity.getName();
        this.description = entity.getDescription();
    }

    @JsonIgnore
    public FlowHistory toEntity() {
        FlowHistory e = new FlowHistory();
        e.setId(getId());
        e.setUuid(getUuid());
        e.setCreatedAt(getCreatedAt());
        e.setUpdatedAt(getUpdatedAt());

        e.setCode(this.code);
        e.setName(this.name);
        e.setDescription(this.description);

        if (Utilities.stringHasValue(getLifeCycleStatus())) {
            e.setLifeCycleStatus(LifeCycleStatus.valueOf(getLifeCycleStatus()));
        }
        return e;
    }
}
