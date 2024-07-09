package mz.org.fgh.mentoring.dto.tutorProgrammaticArea;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

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
            this.setProgrammaticAreaDTO(new ProgrammaticAreaDTO(tutorProgrammaticArea.getProgrammaticArea()));
        }
    }

    public TutorProgrammaticArea toTutorProgrammaticArea() {
        TutorProgrammaticArea tutorProgrammaticArea = new TutorProgrammaticArea();
        tutorProgrammaticArea.setId(this.getId());
        tutorProgrammaticArea.setUpdatedAt(this.getUpdatedAt());
        tutorProgrammaticArea.setUuid(this.getUuid());
        tutorProgrammaticArea.setCreatedAt(this.getCreatedAt());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) tutorProgrammaticArea.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        if(this.getTutorDTO()!=null) tutorProgrammaticArea.setTutor(new Tutor(this.getTutorDTO()));
        if(this.getProgrammaticAreaDTO()!=null) tutorProgrammaticArea.setProgrammaticArea(new ProgrammaticArea(this.getProgrammaticAreaDTO()));
        return tutorProgrammaticArea;
        }

    }
