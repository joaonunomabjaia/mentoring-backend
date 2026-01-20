package mz.org.fgh.mentoring.service.user;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutor.PasswordResetDTO;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.role.UserRoleRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.employee.EmployeeService;
import mz.org.fgh.mentoring.service.role.RoleService;
import mz.org.fgh.mentoring.service.ronda.RondaService;
import mz.org.fgh.mentoring.service.tutor.PasswordResetService;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static mz.org.fgh.mentoring.config.SettingKeys.SERVER_BASE_URL;

@Singleton
public class UserService {

    @Inject
    PasswordResetService passwordResetService;

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
    private final UserRoleRepository userRoleRepository;
    private final SettingService settings;

    public UserService(UserRepository userRepository,
                       EmployeeService employeeService,
                       ProfessionalCategoryRepository professionalCategoryRepository,
                       PartnerRepository partnerRepository,
                       RondaService rondaService,
                       RoleService roleService,
                       EmailSender emailSender,
                       SettingsRepository settingsRepository, UserRoleService userRoleService, UserRoleRepository userRoleRepository, SettingService settings) {
        this.userRepository = userRepository;
        this.employeeService = employeeService;
        this.professionalCategoryRepository = professionalCategoryRepository;
        this.partnerRepository = partnerRepository;
        this.rondaService = rondaService;
        this.roleService = roleService;
        this.emailSender = emailSender;
        this.settingsRepository = settingsRepository;
        this.userRoleService = userRoleService;
        this.userRoleRepository = userRoleRepository;
        this.settings = settings;
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
    public void resetPasswordWithToken(PasswordResetDTO dto) {
        // 1. Verificar se a senha e confirmação coincidem
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }

        // 2. Validar token e obter Employee
        Employee employee = passwordResetService.validateTokenForReset(dto.getToken());

        // 3. Buscar User associado ao Employee
        User user = userRepository.findByEmployee(employee)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para este Employee."));

        // 4. Atualizar senha
        try {
            user.setPassword(Utilities.encryptPassword(dto.getPassword(), user.getSalt()));
            user.setUpdatedAt(DateUtils.getCurrentDate());
            user.setUpdatedBy(user.getUuid());
            userRepository.update(user);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar a senha do usuário.", e);
        }

        // 5. Marcar token como usado
        passwordResetService.consumeTokenAfterReset(dto.getToken());
    }


    @Transactional
    public User create(User user) {
        String password = Utilities.generateRandomPassword(8);

        user.setId(null);
        user.setUuid(UUID.randomUUID().toString());
        user.setCreatedAt(DateUtils.getCurrentDate());
        user.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        user.setSalt(Utilities.generateSalt());

        try {
            user.setPassword(Utilities.encryptPassword(password, user.getSalt()));
            user.setShouldResetPassword(true);
            setupEmployee(user);

            String serverUrl = settings.get(SERVER_BASE_URL, "https://mentdev.csaude.org.mz");
            emailSender.sendEmailToUser(user, password, serverUrl);
        } catch (Exception e) {
            LOG.error("Error encrypting password", e);
            throw new RuntimeException("Error encrypting password", e);
        }

        User created = userRepository.save(user);

        if (Utilities.listHasElements(user.getUserRoles())) {
            userRoleService.create(user.getUserRoles());
        }

        return created;
    }

    private void setupEmployee(User user) {
        Employee employee = user.getEmployee();
        employee.setCreatedBy(user.getCreatedBy());
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

        employeeService.createOrUpdate(employee, user);
        user.setEmployee(employee);
    }

    @Transactional
    public User update(User user) {
        Optional<User> existing = userRepository.findByUuid(user.getUuid());
        if (existing.isEmpty()) {
            throw new RuntimeException("User not found with UUID: " + user.getUuid());
        }

        User toUpdate = existing.get();
        toUpdate.setUsername(user.getUsername());
        toUpdate.setLifeCycleStatus(user.getLifeCycleStatus());
        toUpdate.setUpdatedAt(DateUtils.getCurrentDate());
        toUpdate.setUpdatedBy(user.getUpdatedBy());

        // Atualizações relacionadas (caso existam dentro da entidade User)
        updateUserRoles(user, toUpdate);     // Atualiza as roles com base no novo objeto
        updateEmployee(user, toUpdate);      // Atualiza o empregado vinculado

        return userRepository.update(toUpdate);
    }


    @Transactional
    public void updateUserRoles(User user, User updatedBy) {

        userRoleRepository.deleteByUser(user);
        userRoleService.create(user.getUserRoles());

    }

    @Transactional
    public void updateEmployee(User user, User updatedBy) {
        Employee employee = user.getEmployee();

        if (employee == null || employee.getId() == null) {
            throw new IllegalArgumentException("Employee data is missing or invalid in user");
        }

        Employee existing = employeeService.findById(employee.getId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found with ID: " + employee.getId()));

        existing.setUpdatedBy(updatedBy.getUuid());
        existing.setUpdatedAt(DateUtils.getCurrentDate());
        existing.setEmail(employee.getEmail());
        existing.setName(employee.getName());
        existing.setPhoneNumber(employee.getPhoneNumber());
        existing.setNuit(employee.getNuit());
        existing.setSurname(employee.getSurname());
        existing.setTrainingYear(employee.getTrainingYear());
        existing.setProfessionalCategory(employee.getProfessionalCategory());
        existing.setPartner(employee.getPartner());

        employeeService.update(existing);
    }


    @Transactional
    public void delete(String uuid) {
        Optional<User> existing = userRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            throw new RuntimeException("User not found with UUID: " + uuid);
        }

        User user = existing.get();

        // Remove todos os UserRoles associados ao usuário
        for (UserRole userRole : user.getUserRoles()) {
            userRoleService.deleteUserRole(userRole.getId());
        }

        // Agora remove o próprio usuário
        userRepository.delete(user);
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

    public Page<UserDTO> searchUser(String query, Pageable pageable) {
        Page<User> userPages = userRepository.searchByFilters(query, pageable);
        return userPages.map(UserDTO::new);
    }

    @Deprecated
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

    @Transactional
    public User updateLifeCycleStatus(String uuid, @NotNull LifeCycleStatus lifeCycleStatus, String userUuid) {
        Optional<User> existing = userRepository.findByUuid(uuid);
        if (existing.isEmpty()) {
            throw new RuntimeException("User not found with UUID: " + uuid);
        }

        User user = existing.get();
        user.setLifeCycleStatus(lifeCycleStatus);
        user.setUpdatedAt(DateUtils.getCurrentDate());
        user.setUpdatedBy(userUuid);

        return userRepository.update(user);
    }

    public void updatePassword(String uuid, String newPassword, String updatedByUuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));


        user.setPassword(Utilities.encryptPassword(newPassword, user.getSalt()));
        user.setUpdatedBy(updatedByUuid);
        user.setUpdatedAt(DateUtils.getCurrentDate());

        userRepository.update(user);
    }

    public User getSystemUser() {
        return userRepository.findByUsername("system")
                .orElseThrow(() -> new IllegalStateException("System user not found"));
    }

}
