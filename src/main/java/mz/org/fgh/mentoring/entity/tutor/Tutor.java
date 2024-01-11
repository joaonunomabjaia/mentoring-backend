package mz.org.fgh.mentoring.entity.tutor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.employee.Employee;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Schema(name = "Tutor", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "Tutor")
@Table(name = "tutors")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Tutor extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;
}
