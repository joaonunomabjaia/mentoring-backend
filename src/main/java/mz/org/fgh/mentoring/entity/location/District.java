package mz.org.fgh.mentoring.entity.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Schema(name = "District", description = "Represents a district located in certain a province")
@Entity(name = "district")
@Table(name = "districts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class District extends BaseEntity {

    @Column(name = "DISTRICT", nullable = false, length = 50)
    private String district;

    @Column(name = "PROVINCE", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Province province;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District district1 = (District) o;
        return district.equals(district1.district);
    }

    @Override
    public int hashCode() {
        return Objects.hash(district);
    }
}
