package mz.org.fgh.mentoring.dto.mentorship;

import io.micronaut.core.annotation.Creator;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.answer.AnswerDTO;
import mz.org.fgh.mentoring.dto.form.QuestionDTO;
import mz.org.fgh.mentoring.dto.location.CabinetDTO;
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;
import mz.org.fgh.mentoring.dto.session.SessionDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.Date;
import java.util.List;

@Data
public class MentorshipDTO extends BaseEntityDTO {
    private Integer iterationNumber;
    private Date startDate;
    private Date endDate;
    private TutorDTO mentor;
    private TutoredDTO mentee;
    private SessionDTO session;
    private QuestionDTO form;
    private CabinetDTO cabinet;
    private DoorDTO door;
    private EvaluationTypeDTO evaluationType;
    private List<AnswerDTO> answers;
    private boolean demonstration;
    private String demonstrationDetails;
    private Date performedDate;

    @Creator
    public MentorshipDTO() {
    }

    public MentorshipDTO(Mentorship mentorship) {
        super(mentorship);
        this.setStartDate(mentorship.getStartDate());
        this.setEndDate(mentorship.getEndDate());
        this.setIterationNumber(mentorship.getIterationNumber());
        this.setDemonstration(mentorship.isDemonstration());
        this.setDemonstrationDetails(mentorship.getDemonstrationDetails());
        this.setPerformedDate(mentorship.getPerformedDate());
        if(mentorship.getTutor()!=null) {
            this.setMentor(new TutorDTO());
            this.getMentor().setUuid(mentorship.getTutor().getUuid());
        }
        if(mentorship.getTutored()!=null) {
            this.setMentee(new TutoredDTO());
            this.getMentee().setUuid(mentorship.getTutored().getUuid());
        }
        if(mentorship.getSession()!=null) {
            this.setSession(new SessionDTO());
            this.getSession().setUuid(mentorship.getSession().getUuid());
        }
        if(mentorship.getForm()!=null) {
            this.setForm(new QuestionDTO());
            this.getForm().setUuid(mentorship.getForm().getUuid());
        }
        if(mentorship.getCabinet()!=null) {
            this.setCabinet(new CabinetDTO(mentorship.getCabinet()));
        }
        if(mentorship.getDoor()!=null) {
            this.setDoor(new DoorDTO(mentorship.getDoor()));
        }
        if(mentorship.getEvaluationType()!=null) {
            this.setEvaluationType(new EvaluationTypeDTO(mentorship.getEvaluationType()));
        }
        if(mentorship.getAnswers()!=null) {
            List<AnswerDTO> answerDTOS = Utilities.parse(mentorship.getAnswers(), AnswerDTO.class);
            this.setAnswers(answerDTOS);
        }
    }
}
