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
        this.setCode(question.getCode());
        this.setQuestion(question.getQuestion());
        if(question.getQuestionCategory()!=null) {
            this.setQuestionCategoryDTO(new QuestionCategoryDTO(question.getQuestionCategory()));
        }
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setQuestion(this.getQuestion());
        question.setCode(this.getCode());
        question.setId(this.getId());
        question.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        question.setUuid(this.getUuid());
        question.setCreatedAt(this.getCreatedAt());
        question.setUpdatedAt(this.getUpdatedAt());
        if (this.getQuestionCategoryDTO()!=null) question.setQuestionCategory(new QuestionCategory(this.getQuestionCategoryDTO()));
        return question;
    }


}

