package mz.org.fgh.mentoring.dto.role;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.dto.user.UserDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class UserRoleDTO {

    private UserDTO userDTO;

    private RoleDTO roleDTO;
}
