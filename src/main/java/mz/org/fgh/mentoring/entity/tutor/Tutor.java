package mz.org.fgh.mentoring.entity.tutor;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Schema(name = "Tutor", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "Tutor")
@Table(name = "tutors")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Tutor extends BaseEntity {

    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @Creator
    public Tutor() {}
    public Tutor(TutorDTO tutorDTO) {
        super(tutorDTO);
        this.setEmployee(new Employee(tutorDTO.getEmployeeDTO()));
    }

}
