package mz.org.fgh.mentoring.dto.tutored;

//import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;

import java.util.Date;

@Builder
public record MenteeProgressDTO(
        String uuid,
        String tutoredUuid,
        String tutoredName,
        String tutoredEmail,
        String flowHistoryUuid,
        String flowHistoryName,
        FlowHistoryProgressStatus progressStatus,
        String rondaUuid,
        String rondaName,
        Date createdAt
) {

    public MenteeProgressDTO(MenteeFlowHistory entity) {
        this(
                entity.getUuid(),
                entity.getTutored() != null ? entity.getTutored().getUuid() : null,
                entity.getTutored() != null && entity.getTutored().getEmployee() != null
                        ? entity.getTutored().getEmployee().getFullName() : null,
                entity.getTutored() != null && entity.getTutored().getEmployee() != null
                        ? entity.getTutored().getEmployee().getEmail() : null,
                entity.getFlowHistory() != null ? entity.getFlowHistory().getUuid() : null,
                entity.getFlowHistory() != null ? entity.getFlowHistory().getName() : null,
                entity.getProgressStatus(),
                entity.getRonda() != null ? entity.getRonda().getUuid() : null,
                entity.getRonda() != null ? entity.getRonda().getDescription() : null,
                entity.getCreatedAt()
        );
    }
}
