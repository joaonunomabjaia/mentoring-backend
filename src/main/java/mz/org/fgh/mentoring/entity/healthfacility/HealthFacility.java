package mz.org.fgh.mentoring.entity.healthfacility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.tutor.TutorLocation;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "health_facilities")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class HealthFacility extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRICT_ID", nullable = false)
    private District district;

    @NotEmpty
    @Column(name = "HEALTH_FACILITY", nullable = false, length = 80)
    private String healthFacility;

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    private Set<TutorLocation> tutorLocations = new HashSet<>();
}
