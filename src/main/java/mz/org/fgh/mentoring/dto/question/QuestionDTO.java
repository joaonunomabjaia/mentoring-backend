package mz.org.fgh.mentoring.dto.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionsCategory;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO extends BaseEntityDTO implements Serializable {

    private String code;

    private String question;

    @JsonProperty(value = "questionCategory")
    private QuestionCategoryDTO questionCategoryDTO;

    public QuestionDTO(Question question) {
        super(question);
        this.code = question.getCode();
        this.question = question.getQuestion();
        if(question.getQuestionsCategory()!=null) {
            this.questionCategoryDTO = new QuestionCategoryDTO(question.getQuestionsCategory());
        }
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setQuestion(this.getQuestion());
        question.setCode(this.getCode());
        question.setId(this.getId());
        question.setLifeCycleStatus(this.getLifeCycleStatus());
        QuestionsCategory questionsCategory = this.getQuestionCategoryDTO().toQuestionCategory();
        question.setQuestionsCategory(questionsCategory);
        return question;
    }
}
