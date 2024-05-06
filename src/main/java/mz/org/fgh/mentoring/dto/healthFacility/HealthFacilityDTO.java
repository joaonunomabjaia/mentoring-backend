package mz.org.fgh.mentoring.dto.healthFacility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.district.DistrictDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthFacilityDTO extends BaseEntityDTO {

    private String healthFacility;

    private DistrictDTO districtDTO;

    public HealthFacilityDTO(HealthFacility healthFacility) {
        super(healthFacility);
        this.setHealthFacility(healthFacility.getHealthFacility());
        if (healthFacility.getDistrict() != null && healthFacility.getDistrict().getId() != null) this.setDistrictDTO(new DistrictDTO(healthFacility.getDistrict()));
    }

    public HealthFacility getHealthFacilityObj() {
        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setId(this.getId());
        healthFacility.setUuid(this.getUuid());
        healthFacility.setHealthFacility(this.getHealthFacility());
        if(healthFacility.getDistrict()!=null) {
            healthFacility.setDistrict(this.getDistrictDTO().getDistrict());
        }
        return healthFacility;
    }
}
