package mz.org.fgh.mentoring.service.user;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.employee.EmployeeService;
import mz.org.fgh.mentoring.service.role.RoleService;
import mz.org.fgh.mentoring.service.ronda.RondaService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.*;

@Singleton
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final EmployeeService employeeService;
    private final ProfessionalCategoryRepository professionalCategoryRepository;
    private final PartnerRepository partnerRepository;
    private final RondaService rondaService;
    private final RoleService roleService;
    private final EmailSender emailSender;
    private final SettingsRepository settingsRepository;
    private final UserRoleService userRoleService;

    public UserService(UserRepository userRepository,
                       EmployeeService employeeService,
                       ProfessionalCategoryRepository professionalCategoryRepository,
                       PartnerRepository partnerRepository,
                       RondaService rondaService,
                       RoleService roleService,
                       EmailSender emailSender,
                       SettingsRepository settingsRepository, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.employeeService = employeeService;
        this.professionalCategoryRepository = professionalCategoryRepository;
        this.partnerRepository = partnerRepository;
        this.rondaService = rondaService;
        this.roleService = roleService;
        this.emailSender = emailSender;
        this.settingsRepository = settingsRepository;
        this.userRoleService = userRoleService;
    }

    public UserDTO getByCredentials(User user) {
        // Implement actual logic to get by credentials
        return null;
    }

    public Page<UserDTO> findAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(UserDTO::new);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public User create(User user, Long userId) {
        User authUser = findById(userId);
        String password = Utilities.generateRandomPassword(8);

        user.setId(null);
        user.setCreatedBy(authUser.getUuid());
        user.setUuid(UUID.randomUUID().toString());
        user.setCreatedAt(DateUtils.getCurrentDate());
        user.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        user.setSalt(Utilities.generateSalt());

        try {
            user.setPassword(Utilities.encryptPassword(password, user.getSalt()));
            user.setShouldResetPassword(true);
            setupEmployee(user, authUser);

            String serverUrl = getSettingValue("SERVER_URL");
            emailSender.sendEmailToUser(user, password, serverUrl);
        } catch (Exception e) {
            LOG.error("Error encrypting password", e);
            throw new RuntimeException("Error encrypting password", e);
        }

        return userRepository.save(user);
    }

    private void setupEmployee(User user, User authUser) {
        Employee employee = user.getEmployee();
        employee.setCreatedBy(authUser.getUuid());
        employee.setUuid(UUID.randomUUID().toString());
        employee.setCreatedAt(DateUtils.getCurrentDate());
        employee.setLifeCycleStatus(LifeCycleStatus.ACTIVE);

        employee.setProfessionalCategory(
                professionalCategoryRepository.findByUuid(employee.getProfessionalCategory().getUuid())
                        .orElseThrow(() -> new RuntimeException("Categoria profissional não encontrada"))
        );

        employee.setPartner(
                partnerRepository.findByUuid(employee.getPartner().getUuid())
                        .orElseThrow(() -> new RuntimeException("Parceiro não encontrado"))
        );

        employeeService.createOrUpdate(employee, authUser);
        user.setEmployee(employee);
    }


    private String getSettingValue(String designation) {
        return settingsRepository.findByDesignation(designation)
                .map(Setting::getValue)
                .orElseThrow(() -> new IllegalStateException("Server URL not configured"));
    }

    @Transactional
    public User update(UserDTO userDTO, Long userId) {
        User authUser = findById(userId);
        User userDB = findById(userDTO.getId());

        userDB.setUpdatedBy(authUser.getUuid());
        userDB.setUpdatedAt(DateUtils.getCurrentDate());
        userDB.setLifeCycleStatus(LifeCycleStatus.valueOf(userDTO.getLifeCycleStatus()));
        userDB.setUsername(userDTO.getUsername());

        upDateUserRoles(userDTO, authUser);

        updateEmployee(userDTO, authUser);

        return userRepository.update(userDB);
    }

    @Transactional
    public void upDateUserRoles(UserDTO userDTO, User authUser) {
        Long userId = userDTO.getId();
        List<Long> rolesIds = userDTO.getRoleIds();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));

        for (UserRole userRole : user.getUserRoles()) {
            userRoleService.deleteUserRole(userRole.getId());
        }

        for (Long roleId : rolesIds) {
            userRoleService.create(userId, roleId, authUser.getId());
        }

    }

    private void updateEmployee(UserDTO userDTO, User authUser) {
        Employee employeeDB = employeeService.findById(userDTO.getEmployeeDTO().getId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        employeeDB.setUpdatedBy(authUser.getUuid());
        employeeDB.setUpdatedAt(DateUtils.getCurrentDate());
        employeeDB.setEmail(userDTO.getEmployeeDTO().getEmail());
        employeeDB.setName(userDTO.getEmployeeDTO().getName());
        employeeDB.setPhoneNumber(userDTO.getEmployeeDTO().getPhoneNumber());
        employeeDB.setNuit(userDTO.getEmployeeDTO().getNuit());
        employeeDB.setSurname(userDTO.getEmployeeDTO().getSurname());
        employeeDB.setTrainingYear(userDTO.getEmployeeDTO().getTrainingYear());
        employeeDB.setProfessionalCategory(
                new ProfessionalCategory(userDTO.getEmployeeDTO().getProfessionalCategoryDTO()));
        employeeDB.setPartner(new Partner(userDTO.getEmployeeDTO().getPartnerDTO()));

        employeeService.update(employeeDB);
    }

    @Transactional
    public User delete(User user, Long userId) {
        User authUser = findById(userId);
        user.setLifeCycleStatus(LifeCycleStatus.DELETED);
        user.setUpdatedBy(authUser.getUuid());
        user.setUpdatedAt(DateUtils.getCurrentDate());

        return userRepository.update(user);
    }

    @Transactional
    public User resetPassword(UserDTO userDTO, Long userId) {
        User authUser = findById(userId);
        User userDB = findById(userDTO.getId());

        userDB.setUpdatedBy(authUser.getUuid());
        userDB.setUpdatedAt(DateUtils.getCurrentDate());

        try {
            userDB.setPassword(Utilities.encryptPassword(userDTO.getPassword(), userDB.getSalt()));
            userDB.setShouldResetPassword(userDTO.isShouldResetPassword());
        } catch (Exception e) {
            LOG.error("Error resetting password", e);
            throw new RuntimeException("Error resetting password", e);
        }

        return userRepository.update(userDB);
    }

    @Transactional
    public void destroy(User user) {
        if (!rondaService.doesUserHaveRondas(user) && !roleService.doesUserHaveRoles(user)) {
            userRepository.delete(user);
        }
    }

    public User findByUuid(String uuid) {
        return userRepository.findByUuid(uuid).orElse(null);
    }

    public Page<UserDTO> searchUser(Long userId, String name, String nuit, String userName, Pageable pageable) {
        findById(userId);  // Verify the user exists
        Page<User> userPages = userRepository.search(name, nuit, userName, pageable);
        return userPages.map(UserDTO::new);
    }

    public void updateUserPassword(User user, boolean encrypt) {
        try {
            User userDB = findByUuid(user.getUuid());

            user.setId(userDB.getId());
            user.setCreatedAt(userDB.getCreatedAt());
            user.setCreatedBy(userDB.getCreatedBy());
            user.setUpdatedBy(user.getUuid());
            user.setEmployee(employeeService.getByUuid(user.getEmployee().getUuid()));

            if (encrypt) {
                user.setPassword(Utilities.encryptPassword(user.getPassword(), user.getSalt()));
            }
            userDB.setShouldResetPassword(false);
            userRepository.update(user);
        } catch (Exception e) {
            LOG.error("Error resetting password", e);
            throw new RuntimeException("Error resetting password", e);
        }
    }

    public List<User> findByUuids(List<String> uuids) {
        return userRepository.findByUuidIn(uuids);
    }

    public void updateUserPasswords(List<User> userDTOs, boolean b) {
        for (User user : userDTOs) {
            updateUserPassword(user, b);
        }
    }
}
