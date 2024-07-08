package mz.org.fgh.mentoring.dto.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.province.ProvinceDTO;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.location.Province;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictDTO extends BaseEntityDTO {
    private String description;
    private ProvinceDTO provinceDTO;

    public DistrictDTO(District district) {
        super(district);
        this.setDescription(district.getDescription());
        if (district.getProvince() != null) this.setProvinceDTO(new ProvinceDTO(district.getProvince()));
    }

    public District getDistrict() {
        District district = new District();
        district.setUuid(this.getUuid());
        district.setId(this.getId());
        district.setCreatedAt(this.getCreatedAt());
        district.setUpdatedAt(this.getUpdatedAt());
        district.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        district.setDescription(this.getDescription());
        if(this.getProvinceDTO()!=null) district.setProvince(new Province(this.getProvinceDTO()));
        return district;
    }
}
