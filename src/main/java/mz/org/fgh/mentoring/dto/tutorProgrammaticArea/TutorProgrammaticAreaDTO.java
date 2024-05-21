package mz.org.fgh.mentoring.dto.tutorProgrammaticArea;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@ToString
public class TutorProgrammaticAreaDTO extends BaseEntityDTO {

    private ProgrammaticAreaDTO programmaticAreaDTO;

    private TutorDTO tutorDTO;

    private Long mentorId;

    @Creator
    public TutorProgrammaticAreaDTO() {
        super();
    }

    public TutorProgrammaticAreaDTO(TutorProgrammaticArea tutorProgrammaticArea) {
        super(tutorProgrammaticArea);
        if(tutorProgrammaticArea.getProgrammaticArea()!=null) {
            this.programmaticAreaDTO = new ProgrammaticAreaDTO(tutorProgrammaticArea.getProgrammaticArea());
        }
        /*if(tutorProgrammaticArea.getTutor()!=null) {
            this.tutorDTO = new TutorDTO(tutorProgrammaticArea.getTutor());
        }*/
    }

    public TutorProgrammaticArea toTutorProgrammaticArea() {
            TutorProgrammaticArea tutorProgrammaticArea = new TutorProgrammaticArea();
            if(this.getProgrammaticAreaDTO()!=null) {
                tutorProgrammaticArea.setProgrammaticArea(this.getProgrammaticAreaDTO().toProgrammaticArea());
            }
            /*if(this.getTutorDTO()!=null) {
                tutorProgrammaticArea.setTutor(this.getTutorDTO().toTutor());
            }*/
            return tutorProgrammaticArea;
        }

    }
