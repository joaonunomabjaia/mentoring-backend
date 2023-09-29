package mz.org.fgh.mentoring.dto.programmaticarea;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgrammaticAreaDTO implements Serializable {

    private String uuid;

    private String code;

    private String description;

    private String name;

    public ProgrammaticAreaDTO(ProgrammaticArea programmaticArea) {
        this.uuid = programmaticArea.getUuid();
        this.code = programmaticArea.getCode();
        this.description = programmaticArea.getDescription();
        this.name = programmaticArea.getName();
    }
}
