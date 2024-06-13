package mz.org.fgh.mentoring.service.role;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.role.Role;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.dto.role.RoleDTO;
import mz.org.fgh.mentoring.repository.role.RoleRepository;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDTO> findAllRoles() {
        List<Role> roleList = this.roleRepository.findAll();
        List<RoleDTO> roles = new ArrayList<RoleDTO>();
        for (Role role: roleList) {
            RoleDTO roleDTO = new RoleDTO(role);
            roles.add(roleDTO);
        }
        return roles;
    }

    public boolean doesUserHaveRoles(User user) {
        List<Role> roles = this.roleRepository.getByUserUuid(user.getUuid());
        return !roles.isEmpty();
    }
}
