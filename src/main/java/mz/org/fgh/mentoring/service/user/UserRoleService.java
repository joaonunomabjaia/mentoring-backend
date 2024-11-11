package mz.org.fgh.mentoring.service.user;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.role.UserRoleDTO;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.role.Role;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.role.RoleRepository;
import mz.org.fgh.mentoring.repository.role.UserRoleRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class UserRoleService {
    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRepository userRepository,RoleRepository roleRepository,UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository =roleRepository;
    }
    public List<UserRoleDTO> findAllUserRoles() {
        List<UserRole> userRoles = this.userRoleRepository.findAll();
        List<UserRoleDTO> userRoleDTOS = new ArrayList<UserRoleDTO>();
        for (UserRole userRole: userRoles) {
            UserRoleDTO userDTO = new UserRoleDTO(userRole);
            userRoleDTOS.add(userDTO);
        }
        return userRoleDTOS;
    }

    public void deleteUserRole(Long userRoleId) {

        this.userRoleRepository.deleteById(userRoleId);
    }

    public UserRole findByUserIdAndRoleId(Long userId,Long roleId){
        return this.userRoleRepository.findByUserIdAndRoleId(userId, roleId);
    };

    @Transactional
    public UserRole mergeUserRole(Long userId,Long roleId, Long authUserId) {
        User authUser = userRepository.findById(authUserId).get();
        Role roleDB = roleRepository.findById(roleId).get();
        UserRole userRole;
        try {
            userRole = userRoleRepository.findByUserId(userId);
            userRole.setUpdatedBy(authUser.getUuid());
            userRole.setUpdatedAt(DateUtils.getCurrentDate());
            userRole.setRole(roleDB);
            return userRoleRepository.update(userRole);
        }catch (Exception e){
            userRole = new UserRole();
            userRole.setCreatedBy(authUser.getUuid());
            userRole.setUuid(UUID.randomUUID().toString());
            userRole.setCreatedAt(DateUtils.getCurrentDate());
            userRole.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

            User userDB = userRepository.findById(userId).get();
            userRole.setUser(userDB);
            userRole.setRole(roleDB);

            return this.userRoleRepository.save(userRole);
        }

    }

    @Transactional
    public UserRole create(Long userId,Long roleId, Long authUserId) {
        User authUser = userRepository.findById(authUserId).get();
        Role roleDB = roleRepository.findById(roleId).get();
        User userDB = userRepository.findById(userId).get();

        UserRole userRole = new UserRole();

        userRole.setCreatedBy(authUser.getUuid());
        userRole.setUuid(UUID.randomUUID().toString());
        userRole.setCreatedAt(DateUtils.getCurrentDate());
        userRole.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        userRole.setUser(userDB);
        userRole.setRole(roleDB);

        return this.userRoleRepository.save(userRole);
    }
}
