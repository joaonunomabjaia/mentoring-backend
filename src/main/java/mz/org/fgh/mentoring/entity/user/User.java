package mz.org.fgh.mentoring.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jose Julai Ritsure
 */
@Schema(name = "User", description = "This entity stores data for the user authentication and login")
@Entity(name = "User")
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class User extends BaseEntity {

    public final static String USER_TYPE_TUTOR = "TUTOR";
    public final static String USER_TYPE_TUTORED = "TUTORED";

    @NotEmpty
    @Column(name = "USERNAME", nullable = false, length = 250)
    private String username;

    @NotEmpty
    @Column(name = "PASSWORD", nullable = false, length = 500)
    private String password;

    @NotEmpty
    @Column(name = "SALT", nullable = false, length = 500)
    private String salt;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserRole> userRoles = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
