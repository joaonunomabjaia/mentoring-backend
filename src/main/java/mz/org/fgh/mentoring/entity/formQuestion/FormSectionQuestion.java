package mz.org.fgh.mentoring.entity.formQuestion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.form.FormQuestionDTO;
import mz.org.fgh.mentoring.entity.form.FormSection;
import mz.org.fgh.mentoring.entity.question.EvaluationType;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.ResponseType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Schema(name = "FormSectionQuestion", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "FormSectionQuestion")
@Table(name = "form_section_questions")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class FormSectionQuestion extends BaseEntity {

    @NotNull
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn( name = "FORM_SECTION_ID", nullable = false )
    private FormSection formSection;

    @ToString.Exclude
    @JsonIgnore
    @NotNull
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn( name = "QUESTION_ID", nullable = false )
    private Question question;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESPONSE_TYPE_ID")
    private ResponseType responseType;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EVALUATION_TYPE_ID")
    private EvaluationType evaluationType;

    @Column( name ="SEQUENCE" )
    private Integer sequence;

    public FormSectionQuestion() {
    }

    public FormSectionQuestion(FormQuestionDTO formQuestionDTO) {
        super(formQuestionDTO);
        this.setSequence(formQuestionDTO.getSequence());
        if(formQuestionDTO.getQuestion()!=null) this.setQuestion(new Question(formQuestionDTO.getQuestion()));
        if(formQuestionDTO.getEvaluationType()!=null) this.setEvaluationType(new EvaluationType(formQuestionDTO.getEvaluationType()));
        if(formQuestionDTO.getResponseType()!=null) this.setResponseType(new ResponseType(formQuestionDTO.getResponseType()));

    }

}
