package mz.org.fgh.mentoring.entity.programaticarea;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import javax.persistence.*;
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

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "programmaticArea")
    private Set<TutorProgrammaticArea> tutorProgrammaticAreas = new HashSet<>();
}
