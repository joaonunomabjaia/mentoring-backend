package mz.org.fgh.mentoring.dto.province;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.location.Province;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDTO extends BaseEntityDTO {

    private String designation;

    public ProvinceDTO(Province province) {
        super(province);
        this.setDesignation(province.getDesignation());
    }

    @JsonIgnore
    public Province getProvince() {
        Province province = new Province();
        province.setUuid(this.getUuid());
        province.setId(this.getId());
        province.setCreatedAt(this.getCreatedAt());
        province.setDesignation(this.getDesignation());
        province.setUpdatedAt(this.getUpdatedAt());
        return province;
    }
}
