package mz.org.fgh.mentoring.entity.tutorprogramaticarea;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutorProgrammaticArea.TutorProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.tutor.Tutor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Schema(name = "TutorProgrammaticArea")
@Entity(name = "TutorProgrammaticArea")
@Table(name = "TUTOR_PROGRAMMATIC_AREA", uniqueConstraints = @UniqueConstraint(columnNames = { "TUTOR_ID", "PROGRAMMATIC_AREA_ID"}))
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class TutorProgrammaticArea extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROGRAMMATIC_AREA_ID", nullable = false)
    private ProgrammaticArea programmaticArea;

    @Transient
    private Boolean mapAsUser = Boolean.FALSE;

    @Creator
    public TutorProgrammaticArea(){}
    public TutorProgrammaticArea(TutorProgrammaticAreaDTO tutorProgrammaticAreaDTO){
        super();
        //if (tutorProgrammaticAreaDTO.getTutorDTO() != null) this.tutor =new Tutor(tutorProgrammaticAreaDTO.getTutorDTO());
        this.programmaticArea = new ProgrammaticArea(tutorProgrammaticAreaDTO.getProgrammaticAreaDTO());
    }

    @Override
    public String toString() {
        return "TutorProgrammaticArea{" +
                ", programmaticArea=" + programmaticArea +
                ", mapAsUser=" + mapAsUser +
                '}';
    }
}
