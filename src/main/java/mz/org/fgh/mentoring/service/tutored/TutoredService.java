package mz.org.fgh.mentoring.service.tutored;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.dto.location.LocationDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.location.Province;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.district.DistrictRepository;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.province.ProvinceRepository;
import mz.org.fgh.mentoring.repository.tutored.TutoredRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.util.*;

@Singleton
public class TutoredService {

    private final TutoredRepository tutoredRepository;
    private final UserRepository userRepository;
    private final ProvinceRepository provinceRepository;

    private final DistrictRepository districtRepository;

    private final HealthFacilityRepository healthFacilityRepository;

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    private final PartnerRepository partnerRepository;

    private final LocationRepository locationRepository;

    private final EmployeeRepository employeeRepository;

    public TutoredService(TutoredRepository tutoredRepository, UserRepository userRepository, ProvinceRepository provinceRepository, DistrictRepository districtRepository, HealthFacilityRepository healthFacilityRepository, ProfessionalCategoryRepository professionalCategoryRepository, PartnerRepository partnerRepository, LocationRepository locationRepository, EmployeeRepository employeeRepository) {
        this.tutoredRepository = tutoredRepository;
        this.userRepository = userRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.healthFacilityRepository = healthFacilityRepository;
        this.professionalCategoryRepository = professionalCategoryRepository;
        this.partnerRepository = partnerRepository;
        this.locationRepository = locationRepository;
        this.employeeRepository = employeeRepository;
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

        Employee employee = new Employee(tutoredDTO.getEmployeeDTO());

        Tutored tutored = new Tutored(tutoredDTO);

        employee.setCreatedBy(user.getUuid());
        employee.setCreatedAt(user.getCreatedAt());
        employee.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        employee.setUpdatedAt(DateUtils.getCurrentDate());
        employee.setUpdatedBy(user.getUuid());

        this.employeeRepository.update(employee);

        this.updateLocations(employee.getLocations(), user);

        tutored.setCreatedBy(user.getUuid());
        tutored.setCreatedAt(user.getCreatedAt());
        tutored.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        tutored.setUpdatedAt(DateUtils.getCurrentDate());
        tutored.setUpdatedBy(user.getUuid());
        this.tutoredRepository.update(tutored);

        return tutoredDTO;
    }

    private void updateLocations(Set<Location> locationList, User user){

        for(Location location : locationList){

            location.setCreatedBy(user.getUuid());
            location.setCreatedAt(user.getCreatedAt());
            location.setUpdatedBy(user.getUuid());
            location.setUpdatedAt(DateUtils.getCurrentDate());
            location.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            this.locationRepository.update(location);
        }
    }

}
