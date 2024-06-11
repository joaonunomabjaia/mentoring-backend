package mz.org.fgh.mentoring.entity.session;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.ronda.Ronda;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sessions")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Session extends BaseEntity {
    @NotNull
    @Column(name = "DATE_BEGIN", nullable = false)
    private Date dateBegin;

    @NotNull
    @Column(name = "DATE_END", nullable = false)
    private Date dateEnd;

    @NotNull
    @Column(name = "PERFORMED_DATE", nullable = false)
    private Date performedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SESSION_STATUS_ID", nullable = false)
    private SessionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RONDA_ID", nullable = false)
    private Ronda ronda;

    @Column(name = "REASON")
    private String reason;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
    private List<Mentorship> mentorships;

}
