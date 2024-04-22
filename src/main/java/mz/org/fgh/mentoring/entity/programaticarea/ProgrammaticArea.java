package mz.org.fgh.mentoring.entity.programaticarea;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.program.Program;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
