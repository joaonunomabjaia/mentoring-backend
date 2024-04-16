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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ProgrammaticArea extends BaseEntity {

    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROGRAM_ID", nullable = false)
    private Program program;

    @Creator
    public ProgrammaticArea (){}
    public ProgrammaticArea (ProgrammaticAreaDTO programmaticAreaDTO ){
        this.code=programmaticAreaDTO.getCode();
        this.name=programmaticAreaDTO.getName();
        this.description=programmaticAreaDTO.getDescription();
        this.program= new Program(programmaticAreaDTO.getProgramDTO());
    }

    @Override
    public String toString() {
        return "ProgrammaticArea{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", program=" + program +
                '}';
    }
}
