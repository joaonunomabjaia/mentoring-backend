package mz.org.fgh.mentoring.entity.ronda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ronda")
    private List<RondaMentee> rondaMentees;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ronda")
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

    @JsonIgnore
    public boolean isComplete() {
        return this.endDate != null;
    }
}
