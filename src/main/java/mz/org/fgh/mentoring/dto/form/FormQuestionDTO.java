package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.question.Question;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormQuestionDTO extends BaseEntityDTO implements Serializable {

    @JsonProperty(value = "form")
    private FormDTO formDTO;

    @JsonProperty(value = "question")
    private QuestionDTO questionDTO;

    private boolean mandatory;

    private Integer sequence;

    private Boolean applicable;

    public FormQuestionDTO(FormQuestion formQuestion) {
        super(formQuestion);
        if(formQuestion.getQuestion()!=null) {
            this.questionDTO = new QuestionDTO(formQuestion.getQuestion());
        }
        this.mandatory = formQuestion.isMandatory();
        this.sequence = formQuestion.getSequence();
        this.applicable = formQuestion.getApplicable();
    }

    public FormQuestion toFormQuestion() {
        FormQuestion formQuestion = new FormQuestion();
        formQuestion.setId(this.getId());
        formQuestion.setUuid(this.getUuid());
        formQuestion.setApplicable(this.getApplicable());
        formQuestion.setMandatory(this.isMandatory());
        formQuestion.setSequence(this.getSequence());
        formQuestion.setLifeCycleStatus(this.getLifeCycleStatus());
        if(this.getQuestionDTO()!=null) {
            Question question = this.getQuestionDTO().toQuestion();
            formQuestion.setQuestion(question);
        }
        return formQuestion;
    }
}
