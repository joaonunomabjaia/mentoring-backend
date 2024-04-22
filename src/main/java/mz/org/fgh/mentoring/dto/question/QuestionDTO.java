package mz.org.fgh.mentoring.dto.question;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class QuestionDTO extends BaseEntityDTO implements Serializable {

    private String code;

    private String question;
    
    @Creator
    public QuestionDTO(){}

    @JsonProperty(value = "questionCategory")
    private QuestionCategoryDTO questionCategoryDTO;

    public QuestionDTO(Question question) {
        super(question);
        this.code = question.getCode();
        this.question = question.getQuestion();
        if(question.getQuestionCategory()!=null) {
            this.questionCategoryDTO = new QuestionCategoryDTO(question.getQuestionCategory());
        }
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setQuestion(this.getQuestion());
        question.setCode(this.getCode());
        question.setId(this.getId());
        question.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        QuestionCategory questionCategory = this.getQuestionCategoryDTO().toQuestionCategory();
        question.setQuestionCategory(questionCategory);
        return question;
    }


}

