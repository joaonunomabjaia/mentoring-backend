package mz.org.fgh.mentoring.service.employee;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.repository.district.DistrictRepository;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.healthFacility.HealthFacilityRepository;
import mz.org.fgh.mentoring.repository.location.LocationRepository;
import mz.org.fgh.mentoring.repository.partner.PartnerRepository;
import mz.org.fgh.mentoring.repository.professionalcategory.ProfessionalCategoryRepository;
import mz.org.fgh.mentoring.repository.province.ProvinceRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.Optional;
import java.util.Set;

@Singleton
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final ProvinceRepository provinceRepository;

    private final DistrictRepository districtRepository;

    private final HealthFacilityRepository healthFacilityRepository;

    private final ProfessionalCategoryRepository professionalCategoryRepository;

    private final PartnerRepository partnerRepository;

    private final LocationRepository locationRepository;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository, ProvinceRepository provinceRepository, DistrictRepository districtRepository, HealthFacilityRepository healthFacilityRepository, ProfessionalCategoryRepository professionalCategoryRepository, PartnerRepository partnerRepository, LocationRepository locationRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.healthFacilityRepository = healthFacilityRepository;
        this.professionalCategoryRepository = professionalCategoryRepository;
        this.partnerRepository = partnerRepository;
        this.locationRepository = locationRepository;
    }

    public Employee getById(Long id){
        return this.employeeRepository.findById(id).get();
    }

    public Employee getByUuid(String uuid){
        return this.employeeRepository.findByUuid(uuid).get();
    }


    public Employee createOrUpdate(Employee employee, User user) {
        if (employee == null) throw new RuntimeException("Employee não pode ser nulo");

        if (employee.getPartner() == null || !Utilities.stringHasValue(employee.getPartner().getUuid())) {
            throw new RuntimeException("Instituição laboral é obrigatória.");
        }

        employee.setProfessionalCategory(professionalCategoryRepository.findByUuid(employee.getProfessionalCategory().getUuid()).get());
        employee.setPartner(
                partnerRepository.findByUuid(employee.getPartner().getUuid())
                        .orElseThrow(() -> new RuntimeException("Instituição não encontrada"))
        );
        Employee createdEmployee= employeeRepository.createOrUpdate(employee, user);

        Set<Location> locations =  employee.getLocations();

        if(locations.isEmpty()) {
            throw new RuntimeException("É obrigatório indicar pelo menos uma localização deste Employee!");
        }

        for (Location location : locations) {
            location.setEmployee(createdEmployee);
            if(location.getProvince()!=null)
            {
                location.setProvince(provinceRepository.findByUuid(location.getProvince().getUuid()));
            }
            if(location.getDistrict() !=null)
            {
                location.setDistrict(districtRepository.findByUuid(location.getDistrict().getUuid()));
            }
            if(location.getHealthFacility() !=null)
            {
                location.setHealthFacility(healthFacilityRepository.findByUuid(location.getHealthFacility().getUuid()).get());
            }

            locations.add(location);
        }
      
        locationRepository.createOrUpdate(locations, user);

        return createdEmployee;
    }

    public Optional<Employee> findById(Long id) {
        return this.employeeRepository.findById(id);
    }

    public void update(Employee employeeDB) {
        this.employeeRepository.update(employeeDB);
    }
}
