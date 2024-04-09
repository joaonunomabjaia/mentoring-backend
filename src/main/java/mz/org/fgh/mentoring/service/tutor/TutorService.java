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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class TutorService {

    private final TutorRepository tutorRepository;
    private final UserRepository userRepository;
    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private LocationRepository locationRepository;

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
        return this.tutorRepository.save(tutor);
    }

    public List<Tutor> findTutorWithLimit(long limit, long offset){
      return this.tutorRepository.findTutorWithLimit(limit, offset);
    }

    public List<Tutor> search(String name, Long nuit, Long userId, String phoneNumber) {

        User user = userRepository.findById(userId).get();
        return this.tutorRepository.search(name, nuit, user, phoneNumber);
    }

    public Tutor getTutorByEmployeeUuid(String uuid) {
        return tutorRepository.findByEmployee(employeeRepository.findByUuid(uuid));
    }

    /*public Tutor findTutorByUserUuid(final String userUuid) {
        return this.tutorRepository.findTutorByUserUuid(userUuid);
    }*/
}
