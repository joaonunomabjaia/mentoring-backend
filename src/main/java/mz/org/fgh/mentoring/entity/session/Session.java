package mz.org.fgh.mentoring.entity.session;


import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Session extends BaseEntity {
    @NotNull
    @Column(name = "DATE_BEGIN", nullable = false)
    private LocalDateTime dateBigin;

    @NotNull
    @Column(name = "DATE_END", nullable = false)
    private LocalDateTime dateEnd;

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
