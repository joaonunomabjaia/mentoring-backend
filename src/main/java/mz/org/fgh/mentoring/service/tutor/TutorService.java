package mz.org.fgh.mentoring.service.tutor;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailService;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Singleton
public class TutorService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;
    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private LocationRepository locationRepository;

    @Inject
    private EmailService emailService;

    public TutorService(TutorRepository tutorRepository, UserRepository userRepository) {
        this.tutorRepository = tutorRepository;
        this.userRepository = userRepository;
    }

    public List<Tutor> findAll() {
        return this.tutorRepository.findAll();
    }
    public Optional<Tutor> findById(final Long id){
      return this.tutorRepository.findById(id);
    }

   @Transactional
    public Tutor create(Tutor tutor, Long userId) {
           User user = userRepository.findById(userId).get();
           tutor.setCreatedBy(user.getUuid());
           tutor.setUuid(UUID.randomUUID().toString());
           tutor.setCreatedAt(DateUtils.getCurrentDate());
           tutor.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
           employeeRepository.createOrUpdate(tutor.getEmployee(), user);
           locationRepository.createOrUpdate(tutor.getEmployee().getLocations(), user);
           this.tutorRepository.save(tutor);
           generateMentorUser(tutor, user);
           return tutor;
   }

    private void generateMentorUser(Tutor tutor, User creator) {
        try {
            String password = Utilities.generateRandomPassword(6);
            User user = new User();
            String username = tutor.getEmployee().getName().toLowerCase()+"."+tutor.getEmployee().getSurname().toLowerCase();
            username = generateUserName(username);
            user.setUsername(username);
            user.setEmployee(tutor.getEmployee());
            user.setSalt(UUID.randomUUID().toString());
            user.setPassword(Utilities.MD5Crypt(user.getSalt()+":"+password));
            user.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            user.setUuid(UUID.randomUUID().toString());
            user.setCreatedBy(creator.getUuid());
            user.setCreatedAt(DateUtils.getCurrentDate());
            userRepository.save(user);
            sendmailToUser(user, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateUserName(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            Random random = new Random();
            username += random.nextInt(101);
            generateUserName(username);
        }
        return username;
    }

    private void sendmailToUser(User user, String password) throws Exception {

        String htmlTemplate = emailService.loadHtmlTemplate("emailTemplate");

        // Populate variables
        Map<String, String> variables = new HashMap<>();
        variables.put("name", user.getEmployee().getFullName());
        variables.put("user", user.getUsername());
        variables.put("password", password);

        String populatedHtml = emailService.populateTemplateVariables(htmlTemplate, variables);

        // Send email
        emailService.sendEmail(user.getEmployee().getEmail(), "Registo no Sistema Mentoria", populatedHtml);
    }

    public List<Tutor> findTutorWithLimit(long limit, long offset){
      return this.tutorRepository.findTutorWithLimit(limit, offset);
    }

    public List<Tutor> search(String name, Long nuit, Long userId, String phoneNumber) {

        User user = userRepository.findById(userId).get();
        return this.tutorRepository.search(name, nuit, user, phoneNumber);
    }

    public Tutor getTutorByEmployeeUuid(String uuid) {
        return tutorRepository.findByEmployee(employeeRepository.findByUuid(uuid).get());
    }

    /*public Tutor findTutorByUserUuid(final String userUuid) {
        return this.tutorRepository.findTutorByUserUuid(userUuid);
    }*/
}
