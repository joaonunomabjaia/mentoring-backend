package mz.org.fgh.mentoring.entity.healthfacility;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.tutor.TutorLocation;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "HealthFacility")
@Table(name = "health_facilities")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HealthFacility extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRICT_ID", nullable = false)
    private District district;

    @NotEmpty
    @Column(name = "HEALTH_FACILITY", nullable = false, length = 80)
    private String healthFacility;

}
