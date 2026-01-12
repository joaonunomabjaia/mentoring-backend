package mz.org.fgh.mentoring.dto.tutor;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.entity.tutor.TutorInternalLocation;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorInternalLocationDTO extends BaseEntityDTO {

    private HealthFacilityDTO healthFacilityDTO;
    private Date startDate;
    private Date endDate;

    public TutorInternalLocationDTO(TutorInternalLocation entity) {
        super(entity);
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        if (entity.getLocation() != null) {
            this.healthFacilityDTO = new HealthFacilityDTO(entity.getLocation());
        }
    }

    public TutorInternalLocation toEntity() {
        TutorInternalLocation entity = new TutorInternalLocation();
        entity.setId(this.getId());
        entity.setCreatedAt(this.getCreatedAt());
        entity.setUpdatedAt(this.getUpdatedAt());

        if (Utilities.stringHasValue(this.getLifeCycleStatus())) {
            entity.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        }
        return entity;
    }
}
