package mz.org.fgh.mentoring.entity.answer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.answer.AnswerDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.question.Question;

import javax.persistence.*;
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
    @JoinColumn(name = "MENTORSHIP_ID")
    private Mentorship mentorship;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_QUESTION_ID")
    private FormQuestion formQuestion;

    @Column(name = "TEXT_VALUE", nullable = false)
    private String value;

    public Answer() {
    }
    public Answer(AnswerDTO answerDTO) {
        super(answerDTO);
        this.setValue(answerDTO.getValue());
        if(answerDTO.getForm()!=null) {
            this.setForm(new Form(answerDTO.getForm()));
        }
        if(answerDTO.getQuestion()!=null) {
            this.setQuestion(new Question(answerDTO.getQuestion()));
        }
        if(answerDTO.getMentorship()!=null) {
            this.setMentorship(new Mentorship(answerDTO.getMentorship()));
        }
    }

}
