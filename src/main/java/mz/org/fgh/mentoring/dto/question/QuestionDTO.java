package mz.org.fgh.mentoring.dto.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionType;
import mz.org.fgh.mentoring.entity.question.QuestionsCategory;
import mz.org.fgh.mentoring.entity.question.ResponseType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO extends BaseEntityDTO implements Serializable {

    private String code;

    private String question;

    @JsonProperty(value = "questionType")
    private QuestionTypeDTO questionTypeDTO;

    @JsonProperty(value = "questionCategory")
    private QuestionCategoryDTO questionCategoryDTO;

    @JsonProperty(value = "responseType")
    private ResponseTypeDTO responseTypeDTO;

    public QuestionDTO(Question question) {
        super(question);
        this.code = question.getCode();
        this.question = question.getQuestion();
        if(question.getQuestionType()!=null) {
            this.questionTypeDTO = new QuestionTypeDTO(question.getQuestionType());
        }
        if(question.getQuestionsCategory()!=null) {
            this.questionCategoryDTO = new QuestionCategoryDTO(question.getQuestionsCategory());
        }
        if(question.getResponseType()!=null) {
            this.responseTypeDTO = new ResponseTypeDTO(question.getResponseType());
        }
    }

    public Question toQuestion() {
        Question question = new Question();
        question.setQuestion(this.getQuestion());
        question.setCode(this.getCode());
        question.setId(this.getId());
        question.setLifeCycleStatus(this.getLifeCycleStatus());
        QuestionType questionType = this.getQuestionTypeDTO().toQuestionType();
        question.setQuestionType(questionType);
        QuestionsCategory questionsCategory = this.getQuestionCategoryDTO().toQuestionCategory();
        question.setQuestionsCategory(questionsCategory);
        ResponseType responseType = this.getResponseTypeDTO().toResponseType();
        question.setResponseType(responseType);
        return question;
    }
}
