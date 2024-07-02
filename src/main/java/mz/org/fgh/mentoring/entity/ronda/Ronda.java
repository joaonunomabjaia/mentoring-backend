package mz.org.fgh.mentoring.entity.ronda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Entity(name = "Ronda")
@Table(name = "rondas")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ronda extends BaseEntity {

    @NotEmpty
    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RONDA_TYPE_ID")
    private RondaType rondaType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HEALTH_FACILITY_ID")
    private HealthFacility healthFacility;

    @OneToMany(mappedBy="ronda", fetch = FetchType.LAZY)
    private List<RondaMentee> rondaMentees;

    @OneToMany(mappedBy="ronda", fetch = FetchType.LAZY)
    private List<RondaMentor> rondaMentors;

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Override
    public String toString() {
        return "Ronda{" +
                "description='" + description + '\'' +
                ", rondaType=" + rondaType +
                ", healthFacility=" + healthFacility +
                ", rondaMentees=" + rondaMentees +
                ", rondaMentors=" + rondaMentors +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
