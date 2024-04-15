package mz.org.fgh.mentoring.dto.question;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;

import java.io.Serializable;

/**
 * @author Francisco da Conceicao Alberto Macuacua
 */
@Data
@AllArgsConstructor
public class QuestionCategoryDTO  extends BaseEntityDTO implements Serializable {
    private String category;
    @Creator
    public QuestionCategoryDTO(){}
    public QuestionCategoryDTO(QuestionCategory questionsCategory){
        super(questionsCategory);
        this.category = questionsCategory.getCategory();
    }
}
