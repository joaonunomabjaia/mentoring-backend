package mz.org.fgh.mentoring.dto.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.province.ProvinceDTO;
import mz.org.fgh.mentoring.entity.location.District;

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
        district.setId(this.getId());
        district.setUuid(this.getUuid());
        district.setDescription(this.getDescription());
        if(district.getProvince()!=null) {
            district.setProvince(this.getDistrict().getProvince());
        }
        return district;
    }
}
