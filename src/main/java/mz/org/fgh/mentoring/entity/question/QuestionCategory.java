package mz.org.fgh.mentoring.entity.question;


import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.question.QuestionCategoryDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Schema(name = "QuestionCategory", description = "All possible questions categories")
@Entity(name = "QuestionCategory")
@Table(name = "QUESTION_CATEGORIES")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class QuestionCategory extends BaseEntity {

    @NotEmpty
    @Column(name = "CATEGORY", nullable = false, length = 100)
    private String category;

    @Creator
    public QuestionCategory(){}
    public QuestionCategory(QuestionCategoryDTO questionCategoryDTO){
        super(questionCategoryDTO);
        this.category = questionCategoryDTO.getCategory();
    }

    @Override
    public String toString() {
        return "QuestionCategory{" +
                "category='" + category + '\'' +
                '}';
    }
}
