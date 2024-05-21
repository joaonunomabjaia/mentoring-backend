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
        this.id = programmaticArea.getId();
        this.code = programmaticArea.getCode();
        this.description = programmaticArea.getDescription();
        this.name = programmaticArea.getName();

        if (programmaticArea.getProgram()!= null) {
            if (programmaticArea.getProgram().getName() != null) this.setProgram(new ProgramDTO(programmaticArea.getProgram()));
        }
    }

    public ProgrammaticArea toProgrammaticArea() {
        ProgrammaticArea programmaticArea = new ProgrammaticArea();
        programmaticArea.setCode(this.getCode());
        programmaticArea.setDescription(this.getDescription());
        programmaticArea.setId(this.getId());
        programmaticArea.setUuid(this.getUuid());
        return programmaticArea;
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
