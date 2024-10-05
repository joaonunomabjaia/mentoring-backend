package mz.org.fgh.mentoring.entity.program;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author Jose Julai Ritsure
 */
@Schema(name = "Program", description = "A program of a programmatic area")
@Entity(name = "Program")
@Table(name = "programs")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Program extends BaseEntity {

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "CODE", nullable = false)
    private String code;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "program")
    private List<ProgrammaticArea> programmaticAreas;

    @Creator
    public Program(){}

    public Program(ProgramDTO programDTO) {
        super(programDTO);
        this.setName(programDTO.getName());
        this.setDescription(programDTO.getDescription());
        this.setCode(programDTO.getCode());
    }

    @Override
    public String toString() {
        return "Program{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
