package mz.org.fgh.mentoring.dto.session;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.session.SessionStatus;

@Data
@NoArgsConstructor
public class SessionStatusDTO extends BaseEntityDTO {
    private String code;
    private String description;
    public SessionStatusDTO(SessionStatus sessionStatus) {
        super(sessionStatus);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SessionStatus getSessionStatus() {
        SessionStatus sessionStatus = new SessionStatus();
        sessionStatus.setId(this.getId());
        sessionStatus.setUuid(this.getUuid());
        sessionStatus.setCode(this.getCode());
        sessionStatus.setDescription(this.getDescription());
        return sessionStatus;
    }
}
