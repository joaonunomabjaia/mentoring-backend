package mz.org.fgh.mentoring.entity.programaticarea;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Schema(name = "ProgramaticArea", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "ProgramaticArea")
@Table(name = "programmatic_areas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class ProgrammaticArea extends BaseEntity {

    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRAM_ID", nullable = false)
    private Program program;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "programmaticArea")
    private Set<TutorProgrammaticArea> tutorProgrammaticAreas = new HashSet<>();

    @Creator
    public ProgrammaticArea (ProgrammaticAreaDTO programmaticAreaDTO ){
        this.code=programmaticAreaDTO.getCode();
        this.name=programmaticAreaDTO.getName();
        this.description=programmaticAreaDTO.getDescription();
        this.program= new Program(programmaticAreaDTO.getProgramDTO());
    }
}
