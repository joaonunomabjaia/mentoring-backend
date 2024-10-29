package mz.org.fgh.mentoring.entity.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.mentorship.DoorDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author Jose Julai Ritsure
 */
@Entity
@Table(name = "doors")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class Door extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    public Door() {

    }

    public Door(String uuid) {
        super(uuid);
    }

    public Door(DoorDTO doorDTO) {
        super(doorDTO);
        this.setCode(doorDTO.getCode());
        this.setDescription(doorDTO.getDescription());
    }
}
