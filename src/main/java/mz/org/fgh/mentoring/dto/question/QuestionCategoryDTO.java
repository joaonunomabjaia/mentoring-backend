package mz.org.fgh.mentoring.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;
import java.io.Serializable;

import io.micronaut.core.annotation.Creator;

/**
 * @author Jose Julai Ritsure
 */
@Data
@AllArgsConstructor
public class QuestionCategoryDTO extends BaseEntityDTO implements Serializable {

    private String category;
    
    @Creator
    public QuestionCategoryDTO(){}

    public QuestionCategoryDTO(QuestionCategory questionCategory) {
        super(questionCategory);
        this.category = questionCategory.getCategory();
    }

    public QuestionCategory toQuestionCategory() {
        QuestionCategory questionsCategory = new QuestionCategory();
        questionsCategory.setId(this.getId());
        questionsCategory.setUuid(this.getUuid());
        questionsCategory.setCategory(this.getCategory());
        questionsCategory.setLifeCycleStatus(this.getLifeCycleStatus());
        return questionsCategory;
    }
}
