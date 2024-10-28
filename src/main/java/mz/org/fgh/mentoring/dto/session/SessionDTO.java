package mz.org.fgh.mentoring.dto.session;

import io.micronaut.core.annotation.Creator;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.form.QuestionDTO;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.Date;
import java.util.List;

@Data
public class SessionDTO extends BaseEntityDTO {

    private Date startDate;
    private Date endDate;
    private Date performedDate;
    private SessionStatusDTO sessionStatus;
    private RondaDTO ronda;
    private String reason;
    private QuestionDTO form;
    private TutoredDTO mentee;
    private String strongPoints;
    private String pointsToImprove;
    private String workPlan;
    private String observations;
    private List<MentorshipDTO> mentorships;
    private Date nextSessionDate;

    @Creator
    public SessionDTO() {
    }

    public SessionDTO(Session session) {
        super(session);
        this.setStartDate(session.getStartDate());
        this.setEndDate(session.getEndDate());
        this.setPerformedDate(session.getPerformedDate());
        this.setPointsToImprove(session.getPointsToImprove());
        this.setStrongPoints(session.getStrongPoints());
        this.setObservations(session.getObservations());
        this.setNextSessionDate(session.getNextSessionDate());
        if(session.getStatus()!=null) {
            this.setSessionStatus(new SessionStatusDTO(session.getStatus()));
        }
        if(session.getForm()!=null) {
            this.setForm(new QuestionDTO());
            this.getForm().setUuid(session.getForm().getUuid());
        }
        if(session.getRonda()!=null) {
            this.setRonda(new RondaDTO());
            this.getRonda().setUuid(session.getRonda().getUuid());
        }
        if(session.getMentee()!=null) {
            this.setMentee(new TutoredDTO(session.getMentee()));
        }
        if (Utilities.listHasElements(session.getMentorships())) {
            setMentorships(Utilities.parse(session.getMentorships(), MentorshipDTO.class));
        }
    }
}
