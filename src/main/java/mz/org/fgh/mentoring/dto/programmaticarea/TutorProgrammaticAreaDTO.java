package mz.org.fgh.mentoring.dto.programmaticarea;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorProgrammaticAreaDTO implements Serializable {

    private String uuid;

    @JsonProperty(value = "programmaticArea")
    private ProgrammaticAreaDTO programmaticAreaDTO;

    @JsonProperty(value = "tutor")
    private TutorDTO tutorDTO;

    public TutorProgrammaticAreaDTO(TutorProgrammaticArea tutorProgrammaticArea) {
        this.uuid = tutorProgrammaticArea.getUuid();
        this.programmaticAreaDTO = new ProgrammaticAreaDTO(tutorProgrammaticArea.getProgrammaticArea());
        this.tutorDTO = new TutorDTO(tutorProgrammaticArea.getTutor());
    }
}
