package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.ResponseType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FormQuestionDTO extends BaseEntityDTO implements Serializable {

    @JsonProperty(value = "form")
    private FormDTO form;

    @JsonProperty(value = "question")
    private QuestionDTO question;

    @JsonProperty(value = "evaluationType")
    private EvaluationTypeDTO evaluationType;

    @JsonProperty(value = "responseType")
    private ResponseTypeDTO responseType;

    private Integer sequence;

    private String formUuid;

    @Creator
    public FormQuestionDTO() {
        super();
    }

    public FormQuestionDTO(FormQuestion formQuestion) {
        super(formQuestion);
        this.setSequence(formQuestion.getSequence());
        if(formQuestion.getQuestion()!=null) {
            this.setQuestion(new QuestionDTO(formQuestion.getQuestion()));
        }
        if(formQuestion.getEvaluationType()!=null) {
            this.setEvaluationType(new EvaluationTypeDTO(formQuestion.getEvaluationType()));
        }
        if(formQuestion.getResponseType()!=null) {
            this.setResponseType(new ResponseTypeDTO(formQuestion.getResponseType()));
        }
        if(formQuestion.getForm()!=null) {
            this.setFormUuid(formQuestion.getForm().getUuid());
        }
    }

    public FormQuestion getFormQuestion() {
        FormQuestion formQuestion = new FormQuestion();
        formQuestion.setUuid(this.getUuid());
        formQuestion.setId(this.getId());
        formQuestion.setCreatedAt(this.getCreatedAt());
        formQuestion.setUpdatedAt(this.getUpdatedAt());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) formQuestion.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        formQuestion.setSequence(this.getSequence());
        if(this.getQuestion()!=null) {
            formQuestion.setQuestion(new Question(this.getQuestion()));
        }
        if(this.getEvaluationType()!=null) {
            formQuestion.setEvaluationType(new EvaluationType(this.getEvaluationType()));
        }
        if(this.getResponseType()!=null) {
            formQuestion.setResponseType(new ResponseType(this.getResponseType()));
        }
        return formQuestion;
    }
}
