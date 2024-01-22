package mz.org.fgh.mentoring.dto.province;

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
}
