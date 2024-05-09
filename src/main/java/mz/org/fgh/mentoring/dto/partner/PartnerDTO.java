package mz.org.fgh.mentoring.dto.partner;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.partner.Partner;

@Data
@AllArgsConstructor
public class PartnerDTO extends BaseEntityDTO {
    private String name;

    private String description;

    @Creator
    public PartnerDTO() {
        super();
    }

    public PartnerDTO(Partner partner){
        super(partner);
        this.setName(partner.getName());
        this.setDescription(partner.getDescription());
    }

    public Partner getPartner() {
        Partner partner = new Partner();
        partner.setId(this.getId());
        partner.setUuid(this.getUuid());
        partner.setDescription(this.getDescription());
        partner.setName(this.getName());
        return partner;
    }
}
