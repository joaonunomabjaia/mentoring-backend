package mz.org.fgh.mentoring.entity.location;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "districts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class District extends BaseEntity {

    @NotNull
    @Column(name = "PROVINCE", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Province province;

    @NotEmpty
    @Column(name = "DISTRICT", nullable = false, length = 50)
    private String district;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        District district = (District) o;
        return getId() != null && Objects.equals(getId(), district.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
