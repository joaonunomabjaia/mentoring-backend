package mz.org.fgh.mentoring.dto.programmaticarea;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;

@Data
@AllArgsConstructor
@Introspected
public class ProgrammaticAreaDTO extends BaseEntityDTO {

    private Long id;

    private String code;

    private String description;

    private String name;

    private ProgramDTO program;

    @Creator
    public ProgrammaticAreaDTO() {
        super();
    }

    public ProgrammaticAreaDTO(ProgrammaticArea programmaticArea) {
        super(programmaticArea);
        this.setCode(programmaticArea.getCode());
        this.setDescription(programmaticArea.getDescription());
        this.setName(programmaticArea.getName());

        if (programmaticArea.getProgram()!= null) {
            this.setProgram(new ProgramDTO(programmaticArea.getProgram()));
        }
    }

    @Override
    public String toString() {
        return "ProgrammaticAreaDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", program=" + program +
                '}';
    }
}
