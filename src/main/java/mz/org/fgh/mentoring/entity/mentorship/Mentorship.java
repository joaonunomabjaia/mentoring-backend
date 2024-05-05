package mz.org.fgh.mentoring.entity.mentorship;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Schema(name = "Mentorship", description = "The outcome of the provided mentoring to the tutored individuals")
@Entity(name = "mentorship")
@Table(name = "mentorships")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Mentorship extends BaseEntity {

    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE", nullable = false)
    private Date endDate;

    @Column(name = "PERFORMED_DATE", nullable = false)
    private Date performedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTOR_ID", nullable = false)
    private Tutor tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTORED_ID", nullable = false)
    private Tutored tutored;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_ID", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HEALTH_FACILITY_ID", nullable = false)
    private HealthFacility healthFacility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_ID")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CABINET_ID")
    private Cabinet cabinet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITERATION_TYPE_ID", nullable = false)
    private IterationType iterationType;

    @Column(name = "ITERATION_NUMBER")
    private Integer iterationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOOR_ID", nullable = false)
    private Door door;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIME_OF_DAY_ID", nullable = false)
    private TimeOfDay timeOfDay;
}
