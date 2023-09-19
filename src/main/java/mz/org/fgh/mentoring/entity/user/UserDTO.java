package mz.org.fgh.mentoring.entity.user;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.entity.user.UserIndividual;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class UserDTO {

    private String username;

    private String password;

    private String salt;

    private String type;

    private boolean admin;


    private UserIndividual userIndividual;

    public UserDTO(User user) {
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setType(user.getType());
        this.setAdmin(user.isAdmin());
        this.setSalt(user.getSalt());
        this.setUserIndividual(user.getUserIndividual());
    }
}
