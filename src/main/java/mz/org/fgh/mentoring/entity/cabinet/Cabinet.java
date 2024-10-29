package mz.org.fgh.mentoring.entity.cabinet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.location.CabinetDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cabinets")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Cabinet extends BaseEntity {

    @NotNull
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;
    public Cabinet() {
    }

    public Cabinet(String uuid) {
        super(uuid);
    }

    public Cabinet(CabinetDTO dto) {
        super(dto);
        this.setName(dto.getName());
    }
}
