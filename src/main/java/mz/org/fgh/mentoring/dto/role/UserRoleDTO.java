package mz.org.fgh.mentoring.dto.role;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.role.Role;
import mz.org.fgh.mentoring.entity.role.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class UserRoleDTO extends BaseEntityDTO {

    private UserDTO userDTO;

    private RoleDTO roleDTO;

    public UserRoleDTO(UserRole userRole) {
        super(userRole);
        this.setRoleDTO(new RoleDTO(userRole.getRole()));
    }
}
