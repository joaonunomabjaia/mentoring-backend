package mz.org.fgh.mentoring.entity.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.mentorship.IterationTypeDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author Jose Julai Ritsure
 */
@Entity
@Table(name = "iterations_types")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class IterationType extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    public IterationType() {

    }

    public IterationType(IterationTypeDTO iterationTypeDTO) {
        super(iterationTypeDTO);
        this.setCode(iterationTypeDTO.getCode());
        this.setDescription(iterationTypeDTO.getDescription());
    }
}
