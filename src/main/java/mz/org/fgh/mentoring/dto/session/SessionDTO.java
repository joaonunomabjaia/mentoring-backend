package mz.org.fgh.mentoring.dto.session;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.session.Session;

@Data
@NoArgsConstructor
public class SessionDTO extends BaseEntityDTO {
    private Date startDate;
    private Date endDate;
    private Date performedDate;
    private SessionStatusDTO sessionStatus;
    private String reason;
    public SessionDTO(Session session) {
        super(session);
        this.setSessionStatus(new SessionStatusDTO(session.getStatus()));
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

    public Session getSession() {
        Session session = new Session();
        session.setId(this.getId());
        session.setUuid(this.getUuid());
        session.setDateBegin(this.getStartDate());
        session.setDateEnd(this.getEndDate());
        session.setPerformedDate(this.getPerformedDate());
        if(this.getSessionStatus()!=null) {
            session.setStatus(this.getSessionStatus().getSessionStatus());
        }
        return session;
    }
}
