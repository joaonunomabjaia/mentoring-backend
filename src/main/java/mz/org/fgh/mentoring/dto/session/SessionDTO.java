package mz.org.fgh.mentoring.dto.session;

import io.micronaut.core.annotation.Creator;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.session.Session;

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
    private FormDTO form;
    private TutoredDTO mentee;
    private String strongPoints;
    private String pointsToImprove;
    private String workPlan;
    private String observations;
    private List<MentorshipDTO> mentorships;

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
        if(session.getStatus()!=null) {
            this.setSessionStatus(new SessionStatusDTO(session.getStatus()));
        }
        if(session.getForm()!=null) {
            this.setForm(new FormDTO());
            this.getForm().setUuid(session.getForm().getUuid());
        }
        if(session.getRonda()!=null) {
            this.setRonda(new RondaDTO());
            this.getRonda().setUuid(session.getRonda().getUuid());
        }
        if(session.getMentee()!=null) {
            this.setMentee(new TutoredDTO(session.getMentee()));
        }
    }
}
