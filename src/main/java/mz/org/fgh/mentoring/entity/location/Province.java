package mz.org.fgh.mentoring.entity.location;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.province.ProvinceDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity(name = "Province")
@Table(name = "provinces")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class Province extends BaseEntity {

    @NotEmpty
    @Column(name = "DESIGNATION", unique = true, nullable = false, length = 50)
    private String designation;

    @Creator
    public Province(){}
    public Province(ProvinceDTO provinceDTO) {
        super(provinceDTO);
        this.setDesignation(provinceDTO.getDesignation());
    }

    @Override
    public String toString() {
        return "Province{" +
                "designation='" + designation + '\'' +
                '}';
    }
}
