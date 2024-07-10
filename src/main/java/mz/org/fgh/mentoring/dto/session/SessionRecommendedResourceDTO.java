package mz.org.fgh.mentoring.dto.session;

import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.session.SessionRecommendedResource;

import java.util.Date;

@Data
public class SessionRecommendedResourceDTO extends BaseEntityDTO {
    private String sessionUuid;
    private String tutoredUuid;
    private String tutorUuid;
    private String resourceLink;
    private String resourceName;
    private Date dateRecommended;
    private Date notificationDate;
    private String notificationStatus;

    public SessionRecommendedResourceDTO() {
    }

    public SessionRecommendedResourceDTO(SessionRecommendedResource resource) {
        super(resource);
        this.sessionUuid = resource.getSession() != null ? resource.getSession().getUuid() : null;
        this.tutoredUuid = resource.getTutored() != null ? resource.getTutored().getUuid() : null;
        this.tutorUuid = resource.getTutor() != null ? resource.getTutor().getUuid() : null;
        this.resourceLink = resource.getResourceLink();
        this.resourceName = resource.getResourceName();
        this.dateRecommended = resource.getDateRecommended();
        this.notificationDate = resource.getNotificationDate();
        this.notificationStatus = resource.getNotificationStatus() != null ? resource.getNotificationStatus().name() : null;
    }
}