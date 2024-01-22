package mz.org.fgh.mentoring.dto.employee;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.location.LocationDTO;
import mz.org.fgh.mentoring.dto.partner.PartnerDTO;
import mz.org.fgh.mentoring.dto.professionalCategory.ProfessionalCategoryDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.location.Location;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
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

    public EmployeeDTO(Employee employee) {
        super(employee);

        this.setName(employee.getName());
        this.setSurname(employee.getSurname());
        this.setNuit(employee.getNuit());
        this.setProfessionalCategoryDTO(new ProfessionalCategoryDTO(employee.getProfessionalCategory()));
        this.setTrainingYear(employee.getTrainingYear());
        this.setPhoneNumber(employee.getPhoneNumber());
        this.setEmail(employee.getEmail());
        this.setPartnerDTO(new PartnerDTO(employee.getPartner()));
        this.setLocationDTOSet(setLocations(employee.getLocations()));
    }

    private Set<LocationDTO> setLocations(Set<Location> locationSet) {
        Set<LocationDTO> locationDTOSet = new HashSet<LocationDTO>();

        for (Location location : locationSet) {
            locationDTOSet.add(new LocationDTO(location));
        }
        return locationDTOSet;
    }
}
