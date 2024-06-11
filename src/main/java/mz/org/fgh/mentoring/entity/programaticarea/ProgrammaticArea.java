package mz.org.fgh.mentoring.entity.programaticarea;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.programmaticarea.ProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

@Schema(name = "ProgrammaticArea", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "ProgrammaticArea")
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

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROGRAM_ID", nullable = false)
    private Program program;

    @JsonIgnore
    @OneToMany(mappedBy = "programmaticArea")
    private List<Form> forms;

    @JsonIgnore
    @OneToMany(mappedBy = "programmaticArea")
    private List<TutorProgrammaticArea> tutorProgrammaticAreas;

    @Creator
    public ProgrammaticArea (){}
    public ProgrammaticArea (ProgrammaticAreaDTO programmaticAreaDTO ){
        super(programmaticAreaDTO);
        this.code=programmaticAreaDTO.getCode();
        this.name=programmaticAreaDTO.getName();
        this.description=programmaticAreaDTO.getDescription();
        this.program= new Program(programmaticAreaDTO.getProgram());
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
