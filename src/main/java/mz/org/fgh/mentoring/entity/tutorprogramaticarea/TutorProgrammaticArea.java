package mz.org.fgh.mentoring.entity.tutorprogramaticarea;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.programmaticarea.TutorProgrammaticAreaDTO;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TUTOR_ID", nullable = false)
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
        this.tutor =new Tutor(tutorProgrammaticAreaDTO.getTutorDTO());
        this.programmaticArea = new ProgrammaticArea(tutorProgrammaticAreaDTO.getProgrammaticAreaDTO());
    }

    @Override
    public String toString() {
        return "TutorProgrammaticArea{" +
                "tutor=" + tutor +
                ", programmaticArea=" + programmaticArea +
                ", mapAsUser=" + mapAsUser +
                '}';
    }
}
