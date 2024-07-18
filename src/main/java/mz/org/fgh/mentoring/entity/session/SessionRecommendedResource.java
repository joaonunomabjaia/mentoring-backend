package mz.org.fgh.mentoring.entity.session;

import io.micronaut.core.annotation.Creator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.session.SessionRecommendedResourceDTO;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "session_recommended_resource")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Data
public class SessionRecommendedResource extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne
    @JoinColumn(name = "tutored_id", nullable = false)
    private Tutored tutored;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @Column(name = "resource_link", nullable = false)
    private String resourceLink;

    @Column(name = "resource_name", nullable = false)
    private String resourceName;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_recommended", nullable = false)
    private Date dateRecommended;

    @Temporal(TemporalType.DATE)
    @Column(name = "notification_date")
    private Date notificationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_status", nullable = false)
    private NotificationStatus notificationStatus;

    @Column(name = "token", nullable = true)
    private String token;

    public enum NotificationStatus {
        PENDING,
        SENT
    }

    @Creator
    public SessionRecommendedResource() {
    }

    public SessionRecommendedResource(SessionRecommendedResourceDTO dto, Session session, Tutored tutored, Tutor tutor) {
        super(dto);
        this.session = session;
        this.tutored = tutored;
        this.tutor = tutor;
        this.resourceLink = dto.getResourceLink();
        this.resourceName = dto.getResourceName();
        this.dateRecommended = dto.getDateRecommended();
        this.notificationStatus = NotificationStatus.PENDING;
    }
}
