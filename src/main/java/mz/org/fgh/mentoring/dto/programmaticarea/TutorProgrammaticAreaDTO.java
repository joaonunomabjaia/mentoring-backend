package mz.org.fgh.mentoring.dto.programmaticarea;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TutorProgrammaticAreaDTO implements Serializable {
    private Long id;

    private String uuid;

    @JsonProperty(value = "programmaticArea")
    private ProgrammaticAreaDTO programmaticAreaDTO;

    @JsonProperty(value = "tutor")
    private TutorDTO tutorDTO;
    @Creator
    public TutorProgrammaticAreaDTO(){}
    public TutorProgrammaticAreaDTO(TutorProgrammaticArea tutorProgrammaticArea) {
        this.id = tutorProgrammaticArea.getId();
        this.uuid = tutorProgrammaticArea.getUuid();
        this.programmaticAreaDTO = new ProgrammaticAreaDTO(tutorProgrammaticArea.getProgrammaticArea());
        this.tutorDTO = new TutorDTO(tutorProgrammaticArea.getTutor());
    }

    @Override
    public String toString() {
        return "TutorProgrammaticAreaDTO{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", programmaticAreaDTO=" + programmaticAreaDTO +
                ", tutorDTO=" + tutorDTO +
                '}';
    }
}
