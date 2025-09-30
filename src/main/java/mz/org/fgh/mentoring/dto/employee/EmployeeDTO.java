package mz.org.fgh.mentoring.dto.employee;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.location.LocationDTO;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
public class EmployeeDTO extends BaseEntityDTO {

    private String name;

    private String surname;

    private int nuit;

    private ProfessionalCategoryDTO professionalCategoryDTO;

    private int trainingYear;

    private String phoneNumber;

    private String email;

    private PartnerDTO partnerDTO;

    private Set<LocationDTO> locationDTOSet;

    @Creator
    public EmployeeDTO () {

    }
    public EmployeeDTO(Employee employee) {
        super(employee);

        this.setName(employee.getName());
        this.setSurname(employee.getSurname());
        this.setNuit(employee.getNuit());
        if(employee.getProfessionalCategory()!=null) this.setProfessionalCategoryDTO(new ProfessionalCategoryDTO(employee.getProfessionalCategory()));
        this.setTrainingYear(employee.getTrainingYear());
        this.setPhoneNumber(employee.getPhoneNumber());
        this.setEmail(employee.getEmail());
        if(employee.getPartner()!=null) this.setPartnerDTO(new PartnerDTO(employee.getPartner()));
        if(employee.getLocations()!=null) this.setLocationDTOSet(setLocations(employee.getLocations()));
    }

    private Set<LocationDTO> setLocations(Set<Location> locationSet) {
        Set<LocationDTO> locationDTOSet = new HashSet<LocationDTO>();

        if(!locationSet.isEmpty()) {
            for (Location location : locationSet) {
                locationDTOSet.add(new LocationDTO(location));
            }
        }
        return locationDTOSet;
    }

    public Employee toEntity() {
        Employee employee = new Employee();
        employee.setId(this.getId());
        employee.setUuid(this.getUuid());
        employee.setName(this.getName());
        employee.setSurname(this.getSurname());
        employee.setNuit(this.getNuit());
        employee.setTrainingYear(this.getTrainingYear());
        employee.setPhoneNumber(this.getPhoneNumber());
        employee.setEmail(this.getEmail());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) employee.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        if(this.getProfessionalCategoryDTO()!=null) {
            employee.setProfessionalCategory(this.getProfessionalCategoryDTO().toEntity());
        }
        if(this.getPartnerDTO()!=null) {
            employee.setPartner(this.partnerDTO.toEntity());
        }
        if(this.getLocationDTOSet()!=null && !this.getLocationDTOSet().isEmpty()) {
            Set<Location> locations = new TreeSet<>();
            for (LocationDTO locationDTO: this.getLocationDTOSet()) {
                Location location = locationDTO.toEntity();
                locations.add(location);
            }
            employee.setLocations(locations);
        }
        return employee;
    }
}
