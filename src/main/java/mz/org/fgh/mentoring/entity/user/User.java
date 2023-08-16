package mz.org.fgh.mentoring.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Calendar;

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

    @NotEmpty
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @NotEmpty
    @Column(name = "PASSWORD", nullable = false, length = 50)
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
