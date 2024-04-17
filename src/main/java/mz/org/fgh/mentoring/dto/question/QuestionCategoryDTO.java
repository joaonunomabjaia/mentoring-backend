package mz.org.fgh.mentoring.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.QuestionsCategory;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCategoryDTO extends BaseEntityDTO implements Serializable {

    private String category;

    public QuestionCategoryDTO(QuestionsCategory questionsCategory) {
        super(questionsCategory);
        this.category = questionsCategory.getCategory();
    }

    public QuestionsCategory toQuestionCategory() {
        QuestionsCategory questionsCategory = new QuestionsCategory();
        questionsCategory.setId(this.getId());
        questionsCategory.setUuid(this.getUuid());
        questionsCategory.setCategory(this.getCategory());
        questionsCategory.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        return questionsCategory;
    }
}
