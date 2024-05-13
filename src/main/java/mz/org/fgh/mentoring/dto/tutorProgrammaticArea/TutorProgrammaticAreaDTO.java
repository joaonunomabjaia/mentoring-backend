package mz.org.fgh.mentoring.dto.tutorProgrammaticArea;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class TutorProgrammaticAreaDTO extends BaseEntityDTO {
    private Long id;
    private String uuid;
    private ProgrammaticAreaDTO programmaticAreaDTO;
    private TutorDTO tutorDTO;
    private Long mentorId;
    private Date createdAt;
    private String createdBy;

    @Creator
    public TutorProgrammaticAreaDTO() {
        super();
    }

    public TutorProgrammaticAreaDTO(TutorProgrammaticArea tutorProgrammaticArea) {
        super(tutorProgrammaticArea);
        this.id = tutorProgrammaticArea.getId();
        this.uuid = tutorProgrammaticArea.getUuid();
        this.programmaticAreaDTO = new ProgrammaticAreaDTO(tutorProgrammaticArea.getProgrammaticArea());
    }

    public TutorProgrammaticArea toTutorProgrammaticArea(LifeCycleStatus lifeCycleStatus) {
            TutorProgrammaticArea tutorProgrammaticArea = new TutorProgrammaticArea();
            tutorProgrammaticArea.setId(this.getId());
            tutorProgrammaticArea.setUuid(this.getUuid());
            tutorProgrammaticArea.setCreatedAt(this.getCreatedAt());
            tutorProgrammaticArea.setCreatedBy(this.getCreatedBy());
            tutorProgrammaticArea.setLifeCycleStatus(lifeCycleStatus);
            if(this.getProgrammaticAreaDTO()!=null) {
                tutorProgrammaticArea.setProgrammaticArea(this.getProgrammaticAreaDTO().toProgrammaticArea());
            }
            if(this.getTutorDTO()!=null) {
                tutorProgrammaticArea.setTutor(this.getTutorDTO().toTutor());
            }
            return tutorProgrammaticArea;
        }

    }
