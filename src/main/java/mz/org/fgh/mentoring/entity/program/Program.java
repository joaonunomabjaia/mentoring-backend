package mz.org.fgh.mentoring.entity.program;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Jose Julai Ritsure
 */
@Schema(name = "Program", description = "A program of a programmatic area")
@Entity(name = "Program")
@Table(name = "programs")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Program extends BaseEntity {

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Creator
    public Program(){}
    public Program(ProgramDTO programDTO) {
        super(programDTO);
        this.setDescription(programDTO.getDescription());
        this.setName(programDTO.getName());
    }
}
