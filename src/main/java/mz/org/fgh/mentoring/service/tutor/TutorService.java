package mz.org.fgh.mentoring.service.tutor;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutorProgrammaticArea.TutorProgrammaticAreaDTO;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.programaticarea.TutorProgrammaticAreaRepository;
import mz.org.fgh.mentoring.repository.tutor.TutorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.employee.EmployeeService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import mz.org.fgh.util.EmailSender;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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
    private TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository;
    @Inject
    private EmailSender emailSender;

    private EmployeeService employeeService;

    public TutorService(TutorRepository tutorRepository, UserRepository userRepository, TutorProgrammaticAreaRepository tutorProgrammaticAreaRepository, EmployeeService employeeService) {
        this.tutorRepository = tutorRepository;
        this.userRepository = userRepository;
        this.tutorProgrammaticAreaRepository = tutorProgrammaticAreaRepository;
        this.employeeService = employeeService;
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
           if(!tutor.getTutorProgrammaticAreas().isEmpty()){
               tutor.getTutorProgrammaticAreas().forEach(it -> {
                   tutorProgrammaticAreaRepository.createOrUpdate(it, user, tutor);
               });
           }
           generateMentorUser(tutor, user);
           return tutor;
   }

    private void generateMentorUser(Tutor tutor, User creator) {
        try {
            String password = Utilities.generateRandomPassword(8);
            User user = new User();
            String[] partesNames = tutor.getEmployee().getName().toLowerCase().split(" ");
            String[] partesSunNames = tutor.getEmployee().getSurname().toLowerCase().split(" ");
            String username = partesNames[0]+"."+partesSunNames[partesSunNames.length - 1];
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
            emailSender.sendEmailToUser(user, password);
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


    public List<Tutor> findTutorWithLimit(long limit, long offset){
      return this.tutorRepository.findTutorWithLimit(limit, offset);
    }

    public List<Tutor> search(String name, Long nuit, Long userId, String phoneNumber) {

        User user = userRepository.findById(userId).get();
        String nuitString = nuit != null ? nuit.toString() : null;

        return this.tutorRepository.search(name, nuitString, user, phoneNumber);
    }

    public Tutor getTutorByEmployeeUuid(String uuid) {
        Tutor tutor = tutorRepository.findByEmployee(employeeRepository.findByUuid(uuid).get());
        tutor.setTutorProgrammaticAreas(tutorProgrammaticAreaRepository.getAllByTutorId(tutor.getId()));
        return tutor;
    }

    @Transactional
    public Tutor update(Tutor tutor, Long userId) {
        Optional<Tutor> t = tutorRepository.findByUuid(tutor.getUuid());
        tutor.setCreatedAt(t.get().getCreatedAt());
        tutor.setCreatedBy(t.get().getCreatedBy());
        tutor.setLifeCycleStatus(t.get().getLifeCycleStatus());
        tutor.setId(t.get().getId());

        User user = userRepository.findById(userId).get();
        tutor.setUpdatedBy(user.getUuid());
        tutor.setUpdatedAt(DateUtils.getCurrentDate());
        employeeService.createOrUpdate(tutor.getEmployee(), user);
        locationRepository.createOrUpdate(tutor.getEmployee().getLocations(), user);
        this.tutorRepository.update(tutor);
        return tutor;
    }

    /*public Tutor findTutorByUserUuid(final String userUuid) {
        return this.tutorRepository.findTutorByUserUuid(userUuid);
    }*/

    public List<TutorProgrammaticAreaDTO> findTutorByProgrammaticAreaId(Long programmaticAreaUuid) {
        List<TutorProgrammaticAreaDTO> formDTOS = new ArrayList<>();
        List<TutorProgrammaticArea> tutors = tutorProgrammaticAreaRepository.findByProgrammaticAreaId(programmaticAreaUuid);

        for(TutorProgrammaticArea form : tutors){
            formDTOS.add(new TutorProgrammaticAreaDTO(form));
        }

        return formDTOS;
    }

    public Tutor findByUuid(String uuid) {
        return tutorRepository.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("Tutor not found"));
    }
}
