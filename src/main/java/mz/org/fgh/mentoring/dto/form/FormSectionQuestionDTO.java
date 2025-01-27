package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.mentorship.EvaluationLocationDTO;
import mz.org.fgh.mentoring.dto.mentorship.EvaluationTypeDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;


@Data
@AllArgsConstructor
public class FormSectionQuestionDTO extends BaseEntityDTO {

    private Long formSectionId;

    @JsonProperty(value = "questionDTO")
    private QuestionDTO questionDTO;

    @JsonProperty(value = "evaluationType")
    private EvaluationTypeDTO evaluationType;

    @JsonProperty(value = "responseType")
    private ResponseTypeDTO responseType;

    @JsonProperty("in_use")
    private boolean inUse;

    private Integer sequence;

    private String formUuid;

    private String questionUuid;

    private String evaluationTypeUuid;

    private String responseTypeUuid;

    private String formSectionUuid;

    private EvaluationLocationDTO evaluationLocationDTO;

    private String evaluationLocationUuid;

    @Creator
    public FormSectionQuestionDTO() {
        super();
    }

    public FormSectionQuestionDTO(FormSectionQuestion formSectionQuestion) {
        super(formSectionQuestion);
        this.setFormSectionId(formSectionQuestion.getFormSection().getId());
        this.setSequence(formSectionQuestion.getSequence());
        if(formSectionQuestion.getQuestion()!=null) {
            this.setQuestionDTO(new QuestionDTO(formSectionQuestion.getQuestion()));
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

        this.evaluationLocationDTO = new EvaluationLocationDTO(formSectionQuestion.getEvaluationLocation());
        this.evaluationLocationUuid = formSectionQuestion.getEvaluationLocation().getUuid();
    }

    public FormSectionQuestionDTO(FormSectionQuestion formSectionQuestion, boolean inUse) {
        super(formSectionQuestion);
        this.setFormSectionId(formSectionQuestion.getFormSection().getId());
        this.setSequence(formSectionQuestion.getSequence());

        this.setInUse(inUse);

        if(formSectionQuestion.getQuestion()!=null) {
            this.setQuestionDTO(new QuestionDTO(formSectionQuestion.getQuestion()));
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
