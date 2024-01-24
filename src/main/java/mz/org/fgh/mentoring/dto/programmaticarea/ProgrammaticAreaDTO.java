package mz.org.fgh.mentoring.dto.programmaticarea;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgrammaticAreaDTO extends BaseEntityDTO implements Serializable {

    private String code;

    private String description;

    private String name;

    public ProgrammaticAreaDTO(ProgrammaticArea programmaticArea) {
        super(programmaticArea);
        this.code = programmaticArea.getCode();
        this.description = programmaticArea.getDescription();
        this.name = programmaticArea.getName();
    }
}
