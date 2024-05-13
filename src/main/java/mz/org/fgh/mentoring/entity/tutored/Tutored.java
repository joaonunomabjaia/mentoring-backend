package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Schema(name = "Tutoreds", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "Tutoreds")
@Table(name = "tutoreds")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Tutored extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;


    @Creator
    public Tutored() {

    }

    public Tutored(TutoredDTO tutoredDTO) {
        super(tutoredDTO);
        this.setEmployee(new Employee(tutoredDTO.getEmployeeDTO()));
    }

    @Override
    public String toString() {
        return "Tutored{" +
                "employee=" + employee +
                '}';
    }
}
