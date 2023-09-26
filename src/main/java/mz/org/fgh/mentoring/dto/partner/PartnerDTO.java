package mz.org.fgh.mentoring.dto.partner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.partner.Partner;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO implements Serializable {
    private String name;

    private String description;

    public PartnerDTO(Partner partner){
        this.setName(partner.getName());
        this.setDescription(partner.getDescription());
    }
}
