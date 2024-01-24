package mz.org.fgh.mentoring.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.dto.location.LocationDTO;
import mz.org.fgh.mentoring.dto.role.UserRoleDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.entity.user.UserIndividual;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class UserDTO extends BaseEntityDTO {

    private String username;

    private String password;

    private String salt;

    private EmployeeDTO employeeDTO;

    private List<UserRoleDTO> userRoleDTOS;

    public UserDTO(User user) {
        super(user);
        this.setEmployeeDTO(new EmployeeDTO(user.getEmployee()));
        this.setUserRoleDTOS(setUserRoles(user.getUserRoles()));
        this.setUsername(user.getUsername());
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
