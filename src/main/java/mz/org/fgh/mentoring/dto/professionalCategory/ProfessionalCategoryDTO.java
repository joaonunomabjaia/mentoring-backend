package mz.org.fgh.mentoring.dto.professionalCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;

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
        professionalCategory.setUuid(this.getUuid());
        professionalCategory.setCode(this.getCode());
        professionalCategory.setDescription(this.getDescription());
        return professionalCategory;
    }
}
