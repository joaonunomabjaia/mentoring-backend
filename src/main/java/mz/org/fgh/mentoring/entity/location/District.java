package mz.org.fgh.mentoring.entity.location;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.district.DistrictDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity(name = "District")
@Table(name = "districts")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class District extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROVINCE_ID", nullable = false)
    private Province province;

    @NotEmpty
    @Column(name = "DISTRICT", unique = true, nullable = false, length = 50)
    private String description;

    @Creator
    public District(){}
    public District(DistrictDTO districtDTO) {
        super(districtDTO);
        this.setDescription(districtDTO.getDescription());
        if (districtDTO.getProvinceDTO() != null) this.setProvince(new Province(districtDTO.getProvinceDTO()));
    }

    @Override
    public String toString() {
        return "District{" +
                "province=" + province +
                ", description='" + description + '\'' +
                '}';
    }
}
