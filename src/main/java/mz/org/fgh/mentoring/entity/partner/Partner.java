package mz.org.fgh.mentoring.entity.partner;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "partners")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class Partner extends BaseEntity {

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Creator
    public Partner(){}
    public Partner(PartnerDTO partnerDTO) {
        super(partnerDTO);
        this.setName(partnerDTO.getName());
        this.setDescription(partnerDTO.getDescription());
    }
}
