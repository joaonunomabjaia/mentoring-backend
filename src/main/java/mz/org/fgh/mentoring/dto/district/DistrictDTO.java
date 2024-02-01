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
    private String district;
    private ProvinceDTO provinceDTO;

    public DistrictDTO(District district) {
        super(district);
        this.setDistrict(district.getDistrict());
        this.setProvinceDTO(new ProvinceDTO(district.getProvince()));
    }
}
