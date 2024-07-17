package mz.org.fgh.mentoring.service.user;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.employee.EmployeeService;
import mz.org.fgh.mentoring.service.role.RoleService;
import mz.org.fgh.mentoring.service.ronda.RondaService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailSender;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {

    private final UserRepository userRepository;
    @Inject
    private EmployeeService employeeService;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private  ProfessionalCategoryRepository professionalCategoryRepository;

    @Inject
    private  PartnerRepository partnerRepository;

    @Inject
    private RondaService rondaService;

    @Inject
    private RoleService roleService;
    @Inject
    private EmailSender emailSender;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getByCredencials(User user) {
        Optional<User> possibleUser = userRepository.findByUsername(user.getUsername());

        if (possibleUser.isPresent()) {
            if (possibleUser.get().getPassword().equals(user.getPassword())) {
                /*if (possibleUser.get().isTutor()) {
                    possibleUser.get().setUserIndividual(tutorRepository.findByUser(possibleUser.get()));
                }*/
                return null;
            }
        }

        return null;
    }

    public List<UserDTO> findAllUsers() {
        List<User> userList = this.userRepository.findAll();
        List<UserDTO> users = new ArrayList<UserDTO>();
        for (User user: userList) {
            UserDTO userDTO = new UserDTO(user);
            users.add(userDTO);
        }
        return users;
    }
    public User findById(final Long id){
        return this.userRepository.findById(id).get();
    }
    @Transactional
    public User create(User user, Long userId) {
        try {
            User authUser = userRepository.findById(userId).get();
            String password = Utilities.generateRandomPassword(8);

            user.setId(null);
            user.setCreatedBy(authUser.getUuid());
            user.setUuid(UUID.randomUUID().toString());
            user.setCreatedAt(DateUtils.getCurrentDate());
            user.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            user.setSalt(UUID.randomUUID().toString());
            try {
                user.setPassword(Utilities.MD5Crypt(user.getSalt()+":"+password));
                user.setShouldResetPassword(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Employee userEmployee = user.getEmployee();
            userEmployee.setCreatedBy(authUser.getUuid());
            userEmployee.setUuid(UUID.randomUUID().toString());
            userEmployee.setCreatedAt(DateUtils.getCurrentDate());
            userEmployee.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            userEmployee.setProfessionalCategory(professionalCategoryRepository.findByUuid(userEmployee.getProfessionalCategory().getUuid()));
            userEmployee.setPartner(partnerRepository.findByUuid(userEmployee.getPartner().getUuid()));

            Employee employee = employeeService.createOrUpdate(userEmployee,authUser);
            user.setEmployee(employee);

            emailSender.sendEmailToUser(user, password);
                return this.userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public User update(UserDTO userDTO, User userDB,Long userId) {
        try {
            User authUser = userRepository.findById(userId).get();

            userDB.setUpdatedBy(authUser.getUuid());
            userDB.setUpdatedAt(DateUtils.getCurrentDate());
            userDB.setLifeCycleStatus(LifeCycleStatus.valueOf(userDTO.getLifeCycleStatus()));
            userDB.setEmployee(new Employee(userDTO.getEmployeeDTO()));
            userDB.setUsername(userDTO.getUsername());

            Employee employeeDB = employeeRepository.findById(userDTO.getEmployeeDTO().getId()).get();
            employeeDB.setUpdatedBy(authUser.getUuid());
            employeeDB.setUpdatedAt(DateUtils.getCurrentDate());
            employeeDB.setEmail(userDTO.getEmployeeDTO().getEmail());
            employeeDB.setName(userDTO.getEmployeeDTO().getName());
            employeeDB.setPartner(new Partner(userDTO.getEmployeeDTO().getPartnerDTO()));
            employeeDB.setPhoneNumber(userDTO.getEmployeeDTO().getPhoneNumber());
            employeeDB.setNuit(userDTO.getEmployeeDTO().getNuit());
            employeeDB.setProfessionalCategory(new ProfessionalCategory(userDTO.getEmployeeDTO().getProfessionalCategoryDTO()));
            employeeDB.setSurname(userDTO.getEmployeeDTO().getSurname());
            employeeDB.setTrainingYear(userDTO.getEmployeeDTO().getTrainingYear());

            employeeRepository.update(employeeDB);

            return this.userRepository.update(userDB);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public User delete(User user, Long userId) {
        User authUser = userRepository.findById(userId).get();
        user.setLifeCycleStatus(LifeCycleStatus.DELETED);
        user.setUpdatedBy(authUser.getUuid());
        user.setUpdatedAt(DateUtils.getCurrentDate());

        return this.userRepository.update(user);
    }

    @Transactional
    public User resetPassword(UserDTO userDTO, User userDB,Long userId) {
        User authUser = userRepository.findById(userId).get();

        userDB.setUpdatedBy(authUser.getUuid());
        userDB.setUpdatedAt(DateUtils.getCurrentDate());
        try {
            userDB.setPassword(Utilities.MD5Crypt(userDB.getSalt()+":"+userDTO.getPassword()));
            userDB.setShouldResetPassword(userDTO.isShouldResetPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        userDB.setUsername(userDTO.getUsername());

        return this.userRepository.update(userDB);
    }

    @Transactional
    public void destroy(User user) {
        boolean hasRondas = this.rondaService.doesUserHaveRondas(user);
        boolean hasRoles = this.roleService.doesUserHaveRoles(user);
        if(!hasRondas && !hasRoles){
            this.userRepository.delete(user);
        }
    }

    public User findByUuid(String uuid) {
        Optional<User> possibleUser = this.userRepository.findByUuid(uuid);
        return possibleUser.orElse(null);
    }
}
