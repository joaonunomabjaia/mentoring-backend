package mz.org.fgh.mentoring.dto.professionalCategory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

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

    @JsonIgnore
    public ProfessionalCategory getProfessionalCategory() {
        ProfessionalCategory professionalCategory = new ProfessionalCategory();
        professionalCategory.setId(this.getId());
        professionalCategory.setUpdatedAt(this.getUpdatedAt());
        professionalCategory.setCode(this.getCode());
        professionalCategory.setUuid(this.getUuid());
        professionalCategory.setDescription(this.getDescription());
        professionalCategory.setCreatedAt(this.getCreatedAt());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) professionalCategory.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        return professionalCategory;
    }

    @Override
    public String toString() {
        return "ProfessionalCategoryDTO [code=" + code + ", description=" + description + "]";
    }
}

