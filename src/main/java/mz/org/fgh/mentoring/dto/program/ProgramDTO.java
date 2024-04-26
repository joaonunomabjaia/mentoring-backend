package mz.org.fgh.mentoring.dto.program;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.location.LocationDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jose Julai Ritsure
 */
@Data
@AllArgsConstructor
@Introspected
public class ProgramDTO extends BaseEntityDTO implements Serializable {
    private Long id;
    private String name;
    private String description;

    @Creator
    public ProgramDTO () {

    }

    public  ProgramDTO(Program program) {
        super(program);
        this.id = program.getId();
        this.name = program.getName();
        this.description = program.getDescription();
    }
}
