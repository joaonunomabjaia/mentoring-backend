package mz.org.fgh.mentoring.dto.professionalCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalCategoryDTO extends BaseEntityDTO {

    private String code;
    private String description;

    public ProfessionalCategoryDTO(ProfessionalCategory professionalCategory){
        super(professionalCategory);
        this.setCode(professionalCategory.getCode());
        this.setDescription(professionalCategory.getDescription());
    }

    public ProfessionalCategory getProfessionalCategory() {
        ProfessionalCategory professionalCategory = new ProfessionalCategory();
        professionalCategory.setId(this.getId());
        professionalCategory.setUpdatedAt(this.getUpdatedAt());
        professionalCategory.setCode(this.getCode());
        professionalCategory.setUuid(this.getUuid());
        professionalCategory.setDescription(this.getDescription());
        professionalCategory.setCreatedAt(this.getCreatedAt());
        professionalCategory.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        return professionalCategory;
    }
}
