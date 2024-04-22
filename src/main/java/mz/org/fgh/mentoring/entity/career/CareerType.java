package mz.org.fgh.mentoring.entity.career;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.career.CareerTypeDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "CAREER_TYPE")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class CareerType extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    public CareerType(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public CareerType(CareerTypeDTO careerTypeDTO){
        this.setUuid(careerTypeDTO.getUuid());
        this.setCode(careerTypeDTO.getCode());
        this.setDescription(careerTypeDTO.getDescription());
    }
}
