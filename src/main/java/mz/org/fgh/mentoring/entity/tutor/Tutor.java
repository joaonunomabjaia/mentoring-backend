package mz.org.fgh.mentoring.entity.tutor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Schema(name = "Tutor", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "Tutor")
@Table(name = "tutors")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Tutor extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tutor")
    private List<TutorProgrammaticArea> tutorProgrammaticAreas = new ArrayList<>();

    @Creator
    public Tutor() {}
    public Tutor(TutorDTO tutorDTO) {
        super(tutorDTO);
        this.setEmployee(new Employee(tutorDTO.getEmployeeDTO()));
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "employee=" + employee +
                ", tutorProgrammaticAreas=" + tutorProgrammaticAreas +
                '}';
    }
}
