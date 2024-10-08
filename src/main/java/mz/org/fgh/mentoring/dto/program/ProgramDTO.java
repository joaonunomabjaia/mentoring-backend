package mz.org.fgh.mentoring.dto.program;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.program.Program;

import java.io.Serializable;

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
    private String code;

    @Creator
    public ProgramDTO () {

    }

    public  ProgramDTO(Program program) {
        super(program);
        this.setDescription(program.getDescription());
        this.setName(program.getName());
        this.setCode(program.getCode());
    }
}
