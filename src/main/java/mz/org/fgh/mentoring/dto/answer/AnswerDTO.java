package mz.org.fgh.mentoring.dto.answer;

import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.answer.Answer;

/**
 * @author Jose Julai Ritsure
 */
@Data
public class AnswerDTO extends BaseEntityDTO {

    private String value;

    private String formUuid;

    private String mentorshipUuid;

    private String questionUUid;

    private String formSectionQuestionUuid;

    public AnswerDTO() {

    }

    public AnswerDTO(Answer answer) {
        super(answer);
        this.setValue(answer.getValue());
        this.setMentorshipUuid(answer.getMentorship().getUuid());
        this.setFormUuid(answer.getForm().getUuid());
        this.setQuestionUUid(answer.getQuestion().getUuid());
        this.setFormSectionQuestionUuid(answer.getFormSectionQuestion().getUuid());
    }

}
