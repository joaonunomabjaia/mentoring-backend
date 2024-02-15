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

    public List<TutoredDTO> findAll(long limit, long offset){
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

        Employee employee = this.employeeRepository.findById(tutoredDTO.getEmployeeDTO().getId()).get();
        ProfessionalCategory professionalCategory = this.professionalCategoryRepository.findById(tutoredDTO.getEmployeeDTO().getProfessionalCategoryDTO().getId()).get();
        Tutored tutored = this.tutoredRepository.findById(tutoredDTO.getId()).get();

        employee.setName(tutoredDTO.getEmployeeDTO().getName());
        employee.setSurname(tutoredDTO.getEmployeeDTO().getSurname());
        employee.setNuit(tutoredDTO.getEmployeeDTO().getNuit());
        employee.setProfessionalCategory(professionalCategory);
        employee.setTrainingYear(tutoredDTO.getEmployeeDTO().getTrainingYear());
        employee.setPhoneNumber(tutoredDTO.getEmployeeDTO().getPhoneNumber());
        employee.setEmail(tutoredDTO.getEmployeeDTO().getEmail());
        employee.setUpdatedAt(DateUtils.getCurrentDate());
        employee.setUpdatedBy(user.getUuid());
        //employee.setPartner();

        employee = this.getEmploee(tutoredDTO.getEmployeeDTO(), employee);

        this.employeeRepository.update(employee);

        tutored.setEmployee(employee);
        this.tutoredRepository.update(tutored);

        return new TutoredDTO(tutored);
    }

    private Employee getEmploee(EmployeeDTO employeeDTO, Employee employee){


        Set<Location> locations = new HashSet<>();
        for (LocationDTO location : employeeDTO.getLocationDTOSet() ){

            Province province = this.provinceRepository.findById(location.getProvinceDTO().getId()).get();
            District district = this.districtRepository.findById(location.getDistrictDTO().getId()).get();
            HealthFacility healthFacility = this.healthFacilityRepository.findById(location.getHealthFacilityDTO().getId()).get();

            Location locationEntity = this.locationRepository.findById(location.getId()).get();

            locationEntity.setProvince(province);
            locationEntity.setDistrict(district);
            locationEntity.setHealthFacility(healthFacility);
            locationEntity.setLocationLevel(location.getLocationLevel());
            locationEntity.setEmployee(employee);
            locationEntity.setUpdatedAt(employee.getUpdatedAt());
            locationEntity.setUpdatedBy(employee.getUpdatedBy());

            locations.add(locationEntity);

            this.locationRepository.update(locationEntity);
        }

        employee.setLocations(locations);
        return employee;
    }
}
