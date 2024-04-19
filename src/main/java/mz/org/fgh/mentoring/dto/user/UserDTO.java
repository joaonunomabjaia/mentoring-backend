package mz.org.fgh.mentoring.dto.user;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.dto.role.UserRoleDTO;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Introspected
public class UserDTO extends BaseEntityDTO {

    private String username;

    private String password;

    private String salt;

    private EmployeeDTO employeeDTO;

    private List<UserRoleDTO> userRoleDTOS;
    @Creator
    public UserDTO() {}

    public UserDTO(User user) {
        super(user);
        this.setEmployeeDTO(new EmployeeDTO(user.getEmployee()));
        this.setUserRoleDTOS(setUserRoles(user.getUserRoles()));
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setSalt(user.getSalt());
    }

    private List<UserRoleDTO> setUserRoles(List<UserRole> roleSet) {
        List<UserRoleDTO> roleDTOSet = new ArrayList<>();

        for (UserRole userRole : roleSet) {
            roleDTOSet.add(new UserRoleDTO(userRole));
        }
        return roleDTOSet;
    }
}
