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

    public EmployeeDTO getById(Long id){

        Employee employee = this.employeeRepository.findById(id).get();

        return new EmployeeDTO(employee);
    }

    public EmployeeDTO updade(EmployeeDTO employeeDTO){

        Employee employee = this.employeeRepository.update( new Employee(employeeDTO));

        return new EmployeeDTO(employee);
    }

    public void createOrUpdate(Employee employee, User user) {
        employee.setProfessionalCategory(professionalCategoryRepository.findByUuid(employee.getProfessionalCategory().getUuid()));
        employee.setPartner(partnerRepository.findByUuid(employee.getPartner().getUuid()));

        for (Location location : employee.getLocations()) {
            location.setProvince(provinceRepository.findByUuid(location.getProvince().getUuid()));
            location.setDistrict(districtRepository.findByUuid(location.getDistrict().getUuid()));
            location.setHealthFacility(healthFacilityRepository.findByUuid(location.getHealthFacility().getUuid()).get());
        }

        employeeRepository.createOrUpdate(employee, user);
        locationRepository.createOrUpdate(employee.getLocations(), user);
    }
}
