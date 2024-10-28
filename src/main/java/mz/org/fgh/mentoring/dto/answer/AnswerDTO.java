package mz.org.fgh.mentoring.dto.answer;

import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.form.QuestionDTO;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

/**
 * @author Jose Julai Ritsure
 */
public class AnswerDTO extends BaseEntityDTO {

    private String value;

    private QuestionDTO form;

    private MentorshipDTO mentorship;

    private mz.org.fgh.mentoring.dto.question.QuestionDTO question;

    public AnswerDTO() {

    }

    public AnswerDTO(Answer answer) {
        super(answer);
        this.setValue(answer.getValue());
        if(answer.getMentorship()!=null) {
            this.setMentorship(new MentorshipDTO());
            this.getMentorship().setUuid(answer.getMentorship().getUuid());
        }
        if(answer.getForm()!=null) {
            this.setForm(new QuestionDTO());
            this.getForm().setUuid(answer.getForm().getUuid());
        }
        if(answer.getQuestion()!=null) {
            this.setQuestion(new mz.org.fgh.mentoring.dto.question.QuestionDTO());
            this.getQuestion().setUuid(answer.getQuestion().getUuid());
        }
    }

    public Answer getAnswer() {
        Answer answer = new Answer();
        answer.setUuid(this.getUuid());
        answer.setId(this.getId());
        answer.setCreatedAt(this.getCreatedAt());
        answer.setUpdatedAt(this.getUpdatedAt());
        answer.setValue(this.getValue());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) answer.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        answer.setValue(answer.getValue());
        if(answer.getMentorship()!=null) {
            answer.setMentorship(new Mentorship(this.getMentorship()));
        }
        if(answer.getForm()!=null) {
            answer.setForm(new Form(this.getForm()));
        }
        if(answer.getQuestion()!=null) {
            answer.setQuestion(new Question(this.getQuestion()));
        }
        return answer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public QuestionDTO getForm() {
        return form;
    }

    public void setForm(QuestionDTO form) {
        this.form = form;
    }

    public MentorshipDTO getMentorship() {
        return mentorship;
    }

    public void setMentorship(MentorshipDTO mentorship) {
        this.mentorship = mentorship;
    }

    public mz.org.fgh.mentoring.dto.question.QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(mz.org.fgh.mentoring.dto.question.QuestionDTO question) {
        this.question = question;
    }

}
