package mz.org.fgh.mentoring.entity.formQuestion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.form.FormQuestionDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.ResponseType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Comparator;

@Schema(name = "FormQuestion", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "FormQuestion")
@Table(name = "forms_questions")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class FormQuestion extends BaseEntity {

    @ToString.Exclude
    @NotNull
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn( name = "FORM_ID", nullable = false )
    private Form form;

    @ToString.Exclude
    @JsonIgnore
    @NotNull
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn( name = "QUESTION_ID", nullable = false )
    private Question question;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESPONSE_TYPE_ID")
    private ResponseType responseType;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVALUATION_TYPE_ID")
    private EvaluationType evaluationType;

    @Column( name ="SEQUENCE" )
    private Integer sequence;

    public FormQuestion() {
    }

    public FormQuestion(FormQuestionDTO formQuestionDTO) {
        super(formQuestionDTO);
        this.setSequence(formQuestionDTO.getSequence());
        if(formQuestionDTO.getQuestion()!=null) this.setQuestion(new Question(formQuestionDTO.getQuestion()));
        if(formQuestionDTO.getEvaluationType()!=null) this.setEvaluationType(new EvaluationType(formQuestionDTO.getEvaluationType()));
        if(formQuestionDTO.getResponseType()!=null) this.setResponseType(new ResponseType(formQuestionDTO.getResponseType()));
        if(formQuestionDTO.getForm()!=null) this.setForm(new Form(formQuestionDTO.getForm()));
    }

    public @NotNull Question getQuestion() {
        return question;
    }

    public void setQuestion(@NotNull Question question) {
        this.question = question;
    }

    public @NotNull Form getForm() {
        return form;
    }

    public void setForm(@NotNull Form form) {
        this.form = form;
    }

    public @NotNull ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(@NotNull ResponseType responseType) {
        this.responseType = responseType;
    }

    public @NotNull EvaluationType getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(@NotNull EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}
