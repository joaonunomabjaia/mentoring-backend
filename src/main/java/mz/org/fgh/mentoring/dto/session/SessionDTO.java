package mz.org.fgh.mentoring.dto.session;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.session.Session;

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
            this.setForm(new FormDTO(session.getForm()));
        }
        if(session.getRonda()!=null) {
            this.setRonda(new RondaDTO(session.getRonda()));
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
        return new Session(this);
    }
}
