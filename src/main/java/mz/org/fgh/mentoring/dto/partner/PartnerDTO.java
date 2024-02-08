package mz.org.fgh.mentoring.dto.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.partner.Partner;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO extends BaseEntityDTO {
    private String name;

    private String description;

    public PartnerDTO(Partner partner){
        super(partner);
        this.setName(partner.getName());
        this.setDescription(partner.getDescription());
    }
}
