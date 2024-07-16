package mz.org.fgh.mentoring.dto.session;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.session.SessionStatus;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

@Data
@NoArgsConstructor
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(Date performedDate) {
        this.performedDate = performedDate;
    }

    public SessionStatusDTO getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatusDTO sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RondaDTO getRonda() {
        return ronda;
    }

    public void setRonda(RondaDTO ronda) {
        this.ronda = ronda;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    public TutoredDTO getMentee() {
        return mentee;
    }

    public void setMentee(TutoredDTO mentee) {
        this.mentee = mentee;
    }

    public String getStrongPoints() {
        return strongPoints;
    }

    public void setStrongPoints(String strongPoints) {
        this.strongPoints = strongPoints;
    }

    public String getPointsToImprove() {
        return pointsToImprove;
    }

    public void setPointsToImprove(String pointsToImprove) {
        this.pointsToImprove = pointsToImprove;
    }

    public String getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(String workPlan) {
        this.workPlan = workPlan;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String obsevations) {
        this.observations = obsevations;
    }

    public Session getSession() {
        Session session = new Session();
        session.setId(this.getId());
        session.setUpdatedAt(this.getUpdatedAt());
        session.setUuid(this.getUuid());
        session.setCreatedAt(this.getCreatedAt());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) session.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        session.setStartDate(this.getStartDate());
        session.setEndDate(this.getEndDate());
        session.setPerformedDate(this.getPerformedDate());
        session.setPointsToImprove(this.getPointsToImprove());
        session.setStrongPoints(this.getStrongPoints());
        session.setObservations(this.getObservations());
        if(this.getSessionStatus()!=null) {
            session.setStatus(new SessionStatus(this.getSessionStatus()));
        }
        if(session.getForm()!=null) {
            session.setForm(new Form(this.getForm()));
        }
        if(session.getRonda()!=null) {
            session.setRonda(new Ronda(this.getRonda()));
        }
        if(session.getMentee()!=null) {
            session.setMentee(new Tutored(this.getMentee()));
        }
        return session;
    }
}
