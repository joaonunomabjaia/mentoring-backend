package mz.org.fgh.mentoring.entity.partner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Schema(name = "Partner", description = "An Organization that cooperates with FGH")
@Entity(name = "partner")
@Table(name = "partners")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Partner extends BaseEntity {

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partner partner = (Partner) o;
        return name.equals(partner.name) && description.equals(partner.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
