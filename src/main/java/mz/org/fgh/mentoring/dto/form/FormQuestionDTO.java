package mz.org.fgh.mentoring.dto.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.form.FormSection;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.ResponseType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FormQuestionDTO extends BaseEntityDTO implements Serializable {

    private Long formSectionId;

    @JsonProperty(value = "question")
    private QuestionDTO question;

    @JsonProperty(value = "evaluationType")
    private EvaluationTypeDTO evaluationType;

    @JsonProperty(value = "responseType")
    private ResponseTypeDTO responseType;

    private Integer sequence;

    private FormSectionDTO formSectionDTO;

    private String formUuid;

    @Creator
    public FormQuestionDTO() {
        super();
    }

    public FormQuestionDTO(FormSectionQuestion formSectionQuestion) {
        super(formSectionQuestion);
        this.setFormSectionId(formSectionQuestion.getFormSection().getId());
        this.setSequence(formSectionQuestion.getSequence());
        if(formSectionQuestion.getQuestion()!=null) {
            this.setQuestion(new QuestionDTO(formSectionQuestion.getQuestion()));
        }
        if(formSectionQuestion.getEvaluationType()!=null) {
            this.setEvaluationType(new EvaluationTypeDTO(formSectionQuestion.getEvaluationType()));
        }
        if(formSectionQuestion.getResponseType()!=null) {
            this.setResponseType(new ResponseTypeDTO(formSectionQuestion.getResponseType()));
        }
        if(formSectionQuestion.getFormSection()!=null) {
            this.setFormSectionDTO(new FormSectionDTO(formSectionQuestion.getFormSection()));
        }
    }

    public FormSectionQuestion getFormQuestion() {
        FormSectionQuestion formSectionQuestion = new FormSectionQuestion();
        formSectionQuestion.setUuid(this.getUuid());
        formSectionQuestion.setId(this.getId());
        formSectionQuestion.setCreatedAt(this.getCreatedAt());
        formSectionQuestion.setUpdatedAt(this.getUpdatedAt());
        formSectionQuestion.setCreatedBy(this.getCreatedBy());
        formSectionQuestion.setUpdatedBy(this.getUpdatedBy());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) formSectionQuestion.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        formSectionQuestion.setSequence(this.getSequence());
        if(this.getQuestion()!=null) {
            formSectionQuestion.setQuestion(new Question(this.getQuestion()));
        }
        if(this.getEvaluationType()!=null) {
            formSectionQuestion.setEvaluationType(new EvaluationType(this.getEvaluationType()));
        }
        if(this.getResponseType()!=null) {
            formSectionQuestion.setResponseType(new ResponseType(this.getResponseType()));
        }
        if(this.getFormSectionDTO()!=null) {
            formSectionQuestion.setFormSection(new FormSection(this.getFormSectionDTO()));
        }
        return formSectionQuestion;
    }
}
