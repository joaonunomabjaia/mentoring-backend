package mz.org.fgh.mentoring.entity.location;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Schema(name = "Cabinet", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "cabinet")
@Table(name = "cabinets")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cabinet extends BaseEntity {

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cabinet cabinet = (Cabinet) o;
        return Objects.equals(name, cabinet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
