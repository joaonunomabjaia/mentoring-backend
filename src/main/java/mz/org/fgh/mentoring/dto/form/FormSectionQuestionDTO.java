package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;


@Data
@AllArgsConstructor
public class FormSectionQuestionDTO extends BaseEntityDTO {

    private Long formSectionId;

    @JsonProperty(value = "questionDTO")
    private QuestionDTO question;

    @JsonProperty(value = "evaluationType")
    private EvaluationTypeDTO evaluationType;

    @JsonProperty(value = "responseType")
    private ResponseTypeDTO responseType;

    private Integer sequence;

    private String formUuid;

    private String questionUuid;

    private String evaluationTypeUuid;

    private String responseTypeUuid;

    private String formSectionUuid;

    @Creator
    public FormSectionQuestionDTO() {
        super();
    }

    public FormSectionQuestionDTO(FormSectionQuestion formSectionQuestion) {
        super(formSectionQuestion);
        this.setFormSectionId(formSectionQuestion.getFormSection().getId());
        this.setSequence(formSectionQuestion.getSequence());
        if(formSectionQuestion.getQuestion()!=null) {
            this.setQuestion(new QuestionDTO(formSectionQuestion.getQuestion()));
            this.setQuestionUuid(formSectionQuestion.getQuestion().getUuid());
        }
        if(formSectionQuestion.getEvaluationType()!=null) {
            this.setEvaluationType(new EvaluationTypeDTO(formSectionQuestion.getEvaluationType()));
            this.setEvaluationTypeUuid(formSectionQuestion.getEvaluationType().getUuid());
        }
        if(formSectionQuestion.getResponseType()!=null) {
            this.setResponseType(new ResponseTypeDTO(formSectionQuestion.getResponseType()));
            this.setResponseTypeUuid(formSectionQuestion.getResponseType().getUuid());
        }
        this.setFormSectionUuid(formSectionQuestion.getFormSection().getUuid());
    }

}
