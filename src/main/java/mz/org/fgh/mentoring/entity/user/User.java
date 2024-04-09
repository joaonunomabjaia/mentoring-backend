package mz.org.fgh.mentoring.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.location.District;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.location.Province;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jose Julai Ritsure
 */
@Schema(name = "User", description = "This entity stores data for the user authentication and login")
@Entity(name = "User")
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class User extends BaseEntity {

    @NotEmpty
    @Column(name = "USERNAME", nullable = false, length = 250)
    private String username;

    @NotEmpty
    @Column(name = "PASSWORD", nullable = false, length = 500)
    private String password;

    @NotEmpty
    @Column(name = "SALT", nullable = false, length = 500)
    private String salt;

    @ToString.Exclude
    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRole> userRoles = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public boolean hasNationalAccess() {
        for (UserRole userRole : this.userRoles) {
            if (userRole.getRole().getHierarchyLevel() == 1) return true;
        }
        return false;
    }

    public boolean hasProvincialAccess() {
        for (UserRole userRole : this.userRoles) {
            if (userRole.getRole().getHierarchyLevel() == 2) return true;
        }
        return false;
    }

    public boolean hasDistrictAccess() {
        for (UserRole userRole : this.userRoles) {
            if (userRole.getRole().getHierarchyLevel() == 3) return true;
        }
        return false;
    }

    public boolean hasHFAccess() {
        for (UserRole userRole : this.userRoles) {
            if (userRole.getRole().getHierarchyLevel() == 4) return true;
        }
        return false;
    }

    public List<Province> getGrantedProvinces() {
        List<Province> locations = new ArrayList<>();
        for (Location location : this.employee.getLocations()) {
            if (location.getProvince() != null) locations.add(location.getProvince());
        }
        return locations;
    }

    public List<District> getGrantedDistricts() {
        List<District> locations = new ArrayList<>();
        for (Location location : this.employee.getLocations()) {
            if (location.getDistrict()!= null) locations.add(location.getDistrict());
        }
        return locations;
    }

    public List<HealthFacility> getGrantedHFs() {
        List<HealthFacility> locations = new ArrayList<>();
        for (Location location : this.employee.getLocations()) {
            if (location.getHealthFacility() != null) locations.add(location.getHealthFacility());
        }
        return locations;
    }
}
