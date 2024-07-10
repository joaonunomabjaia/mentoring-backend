package mz.org.fgh.mentoring.entity.session;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.session.SessionDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sessions")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@ToString
public class Session extends BaseEntity {
    @NotNull
    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "PERFORMED_DATE")
    private Date performedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_STATUS_ID", nullable = false)
    private SessionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RONDA_ID", nullable = false)
    private Ronda ronda;

    @Column(name = "REASON")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENTEE_ID", nullable = false)
    private Tutored mentee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORM_ID", nullable = false)
    private Form form;

    @Column(name = "STRONG_POINTS")
    private String strongPoints;

    @Column(name = "POINTS_TO_IMPROVE")
    private String pointsToImprove;

    @Column(name = "WORK_PLAN")
    private String workPlan;

    @Column(name = "OBSERVATIONS")
    private String observations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
    private List<Mentorship> mentorships;

    public Session() {
    }

    public Session(SessionDTO sessionDTO) {
        super(sessionDTO);
        this.setStartDate(sessionDTO.getStartDate());
        this.setEndDate(sessionDTO.getEndDate());
        this.setPerformedDate(sessionDTO.getPerformedDate());
        this.setPointsToImprove(sessionDTO.getPointsToImprove());
        this.setStrongPoints(sessionDTO.getStrongPoints());
        this.setObservations(sessionDTO.getObservations());
        if(sessionDTO.getSessionStatus()!=null) {
            this.setStatus(new SessionStatus(sessionDTO.getSessionStatus()));
        }
        if(sessionDTO.getMentee()!=null) {
            this.setMentee(new Tutored(sessionDTO.getMentee()));
        }
        if(sessionDTO.getRonda()!=null) {
            this.setRonda(new Ronda(sessionDTO.getRonda()));
        }
        if(sessionDTO.getForm()!=null) {
            this.setForm(new Form(sessionDTO.getForm()));
        }
    }
}
