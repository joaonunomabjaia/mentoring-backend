package mz.org.fgh.mentoring.entity.answer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.answer.AnswerDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.question.Question;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Schema(name = "Answer", description = "The outcome provided during a mentoring session")
@Entity(name = "Answer")
@Table(name = "answers")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class Answer extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_ID", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENTORSHIP_ID", nullable = false)
    private Mentorship mentorship;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "QUESTION_ID", nullable = false)
    private Question question;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FORM_SECTION_QUESTION_ID", nullable = false)
    private FormSectionQuestion formSectionQuestion;

    @Column(name = "VALUE", nullable = false)
    private String value;

    public Answer() {
    }
    public Answer(AnswerDTO answerDTO) {
        super(answerDTO);
        this.setValue(answerDTO.getValue());
        this.setForm(new Form(answerDTO.getFormUuid()));
        this.setQuestion(new Question(answerDTO.getQuestionUUid()));
        this.setMentorship(new Mentorship(answerDTO.getMentorshipUuid()));
        this.setFormSectionQuestion(new FormSectionQuestion(answerDTO.getFormSectionQuestionUuid()));
    }

    @Override
    public String toString() {
        return "Answer{" +
                "form=" + form +
                ", question=" + question +
                ", value='" + value + '\'' +
                '}';
    }
}
