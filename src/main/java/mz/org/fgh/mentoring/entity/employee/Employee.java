package mz.org.fgh.mentoring.entity.employee;

import com.sun.istack.NotNull;
import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.dto.location.LocationDTO;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Schema(name = "Employee", description = "A professional that works on an health facility")
@Entity(name = "Employee")
@Table(name = "employee")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Employee extends BaseEntity {

    @NotEmpty
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @NotEmpty
    @Column(name = "SURNAME", nullable = false, length = 50)
    private String surname;

    @NotEmpty
    @Column(name = "NUIT", nullable = false, length = 9)
    private int nuit;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID")
    private ProfessionalCategory professionalCategory;

    @NotEmpty
    @Column(name = "TRAINING_YEAR", length = 4)
    private int trainingYear;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "EMAIL", nullable = false, length = 50)
    @Email
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTNER_ID")
    private Partner partner;

    @NotNull
    @OneToMany(mappedBy="employee", fetch = FetchType.EAGER)
    private Set<Location> locations;

    @Creator
    public Employee () {}
    public Employee(EmployeeDTO employeeDTO) {
        super(employeeDTO);
        this.setName(employeeDTO.getName());
        this.setSurname(employeeDTO.getSurname());
        if(employeeDTO.getLocationDTOSet()!=null) this.setLocations(retriveLocations(employeeDTO.getLocationDTOSet()));
        if(employeeDTO.getPartnerDTO()!=null) this.setPartner(new Partner(employeeDTO.getPartnerDTO()));
        if(employeeDTO.getProfessionalCategoryDTO()!=null) this.setProfessionalCategory(new ProfessionalCategory(employeeDTO.getProfessionalCategoryDTO()));
        this.setEmail(employeeDTO.getEmail());
        this.setNuit(employeeDTO.getNuit());
        this.setTrainingYear(employeeDTO.getTrainingYear());
        this.setPhoneNumber(employeeDTO.getPhoneNumber());
    }

    private Set<Location> retriveLocations(Set<LocationDTO> locationDTOSet) {
        Set<Location> locations = new HashSet<>();
        for (LocationDTO locationDTO : locationDTOSet) {
            Location location = new Location(locationDTO, this);
            locations.add(location);
        }
        return locations;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nuit=" + nuit +
                ", professionalCategory=" + professionalCategory +
                ", trainingYear=" + trainingYear +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", partner=" + partner +
                ", locations=" + locations +
                '}';
    }

    @Transient
    public String getFullName() {
        return this.name +" "+this.surname;
    }
}
