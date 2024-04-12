package mz.org.fgh.mentoring.dto.programmaticarea;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgrammaticAreaDTO extends BaseEntityDTO implements Serializable {

    private Long id;

    private String code;

    private String description;

    private String name;

    private ProgramDTO program;

    public ProgrammaticAreaDTO(ProgrammaticArea programmaticArea) {
        super(programmaticArea);
        this.id = programmaticArea.getId();
        this.code = programmaticArea.getCode();
        this.description = programmaticArea.getDescription();
        this.name = programmaticArea.getName();
        if(programmaticArea.getProgram()!=null) {
            this.program = new ProgramDTO(programmaticArea.getProgram());
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
}
