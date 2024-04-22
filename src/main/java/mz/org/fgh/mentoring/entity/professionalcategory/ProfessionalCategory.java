package mz.org.fgh.mentoring.entity.professionalcategory;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Schema(name = "ProfessionalCategory", description = "Professional Category of an Employee")
@Entity
@Table(name = "professional_category")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProfessionalCategory extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    @Creator
    public ProfessionalCategory(){}
    public ProfessionalCategory(ProfessionalCategoryDTO professionalCategoryDTO) {
        super(professionalCategoryDTO);
        this.setDescription(professionalCategoryDTO.getDescription());
        this.setCode(professionalCategoryDTO.getCode());
    }

    @Override
    public String toString() {
        return "ProfessionalCategory{" +
                "description='" + description + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
