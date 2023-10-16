package mz.org.fgh.mentoring.entity.tutorprogramaticarea;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.tutor.Tutor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Schema(name = "TutorProgrammaticArea")
@Entity(name = "TutorProgrammaticArea")
@Table(name = "TUTOR_PROGRAMMATIC_AREA", uniqueConstraints = @UniqueConstraint(columnNames = { "TUTOR_ID", "PROGRAMMATIC_AREA_ID"}))
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@ToString
public class TutorProgrammaticArea extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTOR_ID", nullable = false)
    private Tutor tutor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAMMATIC_AREA_ID", nullable = false)
    private ProgrammaticArea programmaticArea;

    @Transient
    private Boolean mapAsUser = Boolean.FALSE;

}
