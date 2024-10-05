package mz.org.fgh.mentoring.entity.question;


import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.question.SectionDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Schema(name = "Section", description = "All possible form sections")
@Entity(name = "Section")
@Table(name = "section")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class Section extends BaseEntity {

    @NotEmpty
    @Column(name = "DESCRIPTION", nullable = false, unique = true, length = 200)
    private String description;

    @Creator
    public Section(){}

    public Section(SectionDTO questionCategoryDTO){
        super(questionCategoryDTO);
        this.setDescription(questionCategoryDTO.getDescription());
    }

    @Override
    public String toString() {
        return "Section{" +
                "description='" + description + '\'' +
                '}';
    }
}
