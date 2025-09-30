package mz.org.fgh.mentoring.dto.session;

import lombok.Getter;
import lombok.Setter;
import mz.org.fgh.mentoring.base.BaseEntityDTO;

@Setter
@Getter
public class SessionReportDTO extends BaseEntityDTO {
    // Getters and Setters
    private long totalSessions;
    private long totalInternalMentorSessions;
    private long totalExternalMentorSessions;

    public SessionReportDTO(long totalSessions, long totalInternalMentorSessions, long totalExternalMentorSessions) {
        this.totalSessions = totalSessions;
        this.totalInternalMentorSessions = totalInternalMentorSessions;
        this.totalExternalMentorSessions = totalExternalMentorSessions;
    }

}