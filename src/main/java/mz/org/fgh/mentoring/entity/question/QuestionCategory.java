package mz.org.fgh.mentoring.entity.question;


import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.question.QuestionCategoryDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "QUESTION_CATEGORIES")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class QuestionCategory extends BaseEntity {

    @NotEmpty
    @Column(name = "CATEGORY", nullable = false, length = 100)
    private String category;

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
