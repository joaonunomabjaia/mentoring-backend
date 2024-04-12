package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.dto.question.ResponseTypeDTO;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.ResponseType;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormQuestionDTO extends BaseEntityDTO implements Serializable {

    @JsonProperty(value = "form")
    private FormDTO formDTO;

    @JsonProperty(value = "question")
    private QuestionDTO questionDTO;

    @JsonProperty(value = "evaluationType")
    private EvaluationTypeDTO evaluationTypeDTO;

    @JsonProperty(value = "responseType")
    private ResponseTypeDTO responseTypeDTO;

    private Integer sequence;

    private Date createdAt;

    private String createdBy;

    public FormQuestionDTO(FormQuestion formQuestion) {
        super(formQuestion);
        if(formQuestion.getQuestion()!=null) {
            this.questionDTO = new QuestionDTO(formQuestion.getQuestion());
        }
        if(formQuestion.getResponseType()!=null) {
            this.responseTypeDTO = new ResponseTypeDTO(formQuestion.getResponseType());
        }
        if(formQuestion.getEvaluationType()!=null) {
            this.evaluationTypeDTO = new EvaluationTypeDTO(formQuestion.getEvaluationType());
        }
        this.sequence = formQuestion.getSequence();
        this.createdAt = formQuestion.getCreatedAt();
        this.createdBy = formQuestion.getCreatedBy();
    }

    public FormQuestion toFormQuestion() {
        FormQuestion formQuestion = new FormQuestion();
        formQuestion.setId(this.getId());
        formQuestion.setUuid(this.getUuid());
        formQuestion.setSequence(this.getSequence());
        formQuestion.setLifeCycleStatus(this.getLifeCycleStatus());
        formQuestion.setCreatedAt(this.getCreatedAt());
        formQuestion.setCreatedBy(this.getCreatedBy());
        if(this.getQuestionDTO()!=null) {
            Question question = this.getQuestionDTO().toQuestion();
            formQuestion.setQuestion(question);
        }
        if(this.getEvaluationTypeDTO()!=null) {
            EvaluationType evaluationType = this.getEvaluationTypeDTO().toEvaluationType();
            formQuestion.setEvaluationType(evaluationType);
        }
        if(this.getResponseTypeDTO()!=null) {
            ResponseType responseType = this.getResponseTypeDTO().toResponseType();
            formQuestion.setResponseType(responseType);
        }
        return formQuestion;
    }
}
