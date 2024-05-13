package mz.org.fgh.mentoring.service.tutored;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.employee.EmployeeService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class TutoredService {

    private final EmployeeService employeeService;
    private final TutoredRepository tutoredRepository;
    private final UserRepository userRepository;
    @Inject
    private LocationRepository locationRepository;



    public TutoredService(EmployeeService employeeService, TutoredRepository tutoredRepository, UserRepository userRepository) {
        this.employeeService = employeeService;
        this.tutoredRepository = tutoredRepository;
        this.userRepository = userRepository;
    }

    public List<TutoredDTO> findAll(long offset, long  limit){
        List<Tutored> tutoreds = new ArrayList<>();
        List<TutoredDTO> tutoredDTOS = new ArrayList<>();

        if(limit > 0){
            tutoreds = this.tutoredRepository.findTutoredWithLimit(limit, offset);
        }else {
            tutoreds = this.tutoredRepository.findAll();
        }

        for (Tutored tutored :tutoreds) {
            tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }

    public List<TutoredDTO> findTutorByUserUuid(String tutorUuid){
        List<Tutored> tutoreds = this.tutoredRepository.findTutoredByTutorUuid(tutorUuid);
        List<TutoredDTO> tutoredDTOS = new ArrayList<>(tutoreds.size());

        for (Tutored tutored : tutoreds){
           tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }

    public List<TutoredDTO> findTutoredByUuid(String uuid){
        List<Tutored> tutoreds = this.tutoredRepository.findTutoredByUuid(uuid);
        List<TutoredDTO> tutoredDTOS = new ArrayList<>(tutoreds.size());

        for (Tutored tutored : tutoreds){
            tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }

    public List<TutoredDTO> searchTutored(Long userId ,Long nuit, String name, String phoneNumber) {

        User user = this.userRepository.findById(userId).get();
        List<Tutored> tutoreds = this.tutoredRepository.search(nuit, name,user, phoneNumber);
        List<TutoredDTO> tutoredDTOS = new ArrayList<>(tutoreds.size());

        for (Tutored tutored : tutoreds){
            tutoredDTOS.add(new TutoredDTO(tutored));
        }
        return tutoredDTOS;
    }

    public TutoredDTO getById(Long id){

        Tutored tutored = this.tutoredRepository.findById(id).get();

        return new TutoredDTO(tutored);
    }
    public TutoredDTO update(TutoredDTO tutoredDTO){

       Tutored tutored = this.tutoredRepository.update(new Tutored(tutoredDTO));
       return new TutoredDTO(tutored);
    }

    public TutoredDTO updateTutored(TutoredDTO tutoredDTO, Long userId){
        User user = this.userRepository.findById(userId).get();
        Tutored tutored = new Tutored(tutoredDTO);
        Optional<Tutored> t = tutoredRepository.findByUuid(tutored.getUuid());
        if(t.isPresent()){
            tutored.setCreatedAt(t.get().getCreatedAt());
            tutored.setCreatedBy(t.get().getCreatedBy());
            tutored.setLifeCycleStatus(t.get().getLifeCycleStatus());
            tutored.setId(t.get().getId());
        }

        employeeService.createOrUpdate(tutored.getEmployee(), user);

        tutored.setUpdatedAt(DateUtils.getCurrentDate());
        tutored.setUpdatedBy(user.getUuid());
        this.tutoredRepository.update(tutored);

        return tutoredDTO;
    }

    public List<Tutored> getTutoredsByHealthFacilityUuids(List<String> uuids) {
        return tutoredRepository.getTutoredsByHealthFacilityUuids(uuids);
    }

    @Transactional
    public Tutored create(Tutored tutored, Long userId) {
        User user = userRepository.findById(userId).get();
        tutored.setCreatedBy(user.getUuid());
        tutored.setCreatedAt(DateUtils.getCurrentDate());
        tutored.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        employeeService.createOrUpdate(tutored.getEmployee(), user);
        return this.tutoredRepository.save(tutored);
    }


}
