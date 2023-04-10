package mz.org.fgh.mentoring.entity.session;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Session extends BaseEntity {
    @NotNull
    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

    @NotNull
    @Column(name = "PERFORMED_DATE", nullable = false)
    private LocalDate performedDate;

    @NotNull
    @Column(name = "STATUS", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Column(name = "REASON")
    private String reason;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
    private List<Mentorship> mentorships;

}
