package mz.org.fgh.mentoring.dto.program;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.program.Program;

import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDTO extends BaseEntityDTO implements Serializable {
    private String name;
    private String description;

    public  ProgramDTO(Program program) {
        super(program);
        this.name = program.getName();
        this.description = program.getDescription();
    }
}
