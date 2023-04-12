package mz.org.fgh.mentoring.entity.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Schema(name = "HealthFacility", description = "Represents a health facility located in certain a district")
@Entity(name = "healthFacility")
@Table(name = "health_facilities")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HealthFacility extends BaseEntity {

    @Column(name = "HEALTH_FACILITY", nullable = false, length = 80)
    private String healthFacility;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRICT_ID", nullable = false)
    @ToString.Exclude
    private District district;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthFacility that = (HealthFacility) o;
        return Objects.equals(healthFacility, that.healthFacility);
    }

    @Override
    public int hashCode() {
        return Objects.hash(healthFacility);
    }
}
