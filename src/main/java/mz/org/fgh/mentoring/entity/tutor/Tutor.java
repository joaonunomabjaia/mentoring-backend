package mz.org.fgh.mentoring.entity.tutor;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.entity.user.UserIndividual;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Schema(name = "Tutor", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "Tutor")
@Table(name = "tutors")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Tutor extends Employee {

}
