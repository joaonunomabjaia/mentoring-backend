package mz.org.fgh.mentoring.dto.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.district.DistrictDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.dto.province.ProvinceDTO;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO extends BaseEntityDTO {

    private ProvinceDTO provinceDTO;

    private DistrictDTO districtDTO;

    private HealthFacilityDTO healthFacilityDTO;

    private EmployeeDTO employeeDTO;

    private String locationLevel;

    public LocationDTO(Location location) {
        super(location);
        if (location.getProvince() != null) this.setProvinceDTO(new ProvinceDTO(location.getProvince()));
        if (location.getDistrict() != null) this.setDistrictDTO(new DistrictDTO(location.getDistrict()));
        if (location.getHealthFacility() != null) this.setHealthFacilityDTO(new HealthFacilityDTO(location.getHealthFacility()));
        this.setLocationLevel(location.getLocationLevel());
    }

    public Location toEntity() {
        Location location = new Location();
        location.setId(this.getId());
        location.setUuid(this.getUuid());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) location.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        if(this.getEmployeeDTO()!=null) {
            location.setEmployee(this.employeeDTO.toEntity());
        }
        if(this.getDistrictDTO()!=null) {
            location.setDistrict(this.getDistrictDTO().toEntity());
        }
        if(this.getProvinceDTO()!=null) {
            location.setProvince(this.getProvinceDTO().toEntity());
        }
        if(this.getHealthFacilityDTO()!=null) {
            location.setHealthFacility(this.getHealthFacilityDTO().toEntity());
        }
        return location;
    }
}
