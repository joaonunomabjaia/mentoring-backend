package mz.org.fgh.mentoring.entity.location;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.location.LocationDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "Location")
@Table(name = "location")
@Data
//@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Location extends BaseEntity {
    public static final String LOCATION_LEVEL_NATIONAL = "NATIONAL";
    public static final String LOCATION_LEVEL_PROVINCIAL = "PROVINCIAL";
    public static final String LOCATION_LEVEL_DISTRITAL = "DISTRITAL";
    public static final String LOCATION_LEVEL_HEALTH_FACILITY = "HEALTH_FACILITY";


    @EqualsAndHashCode.Include
    @ManyToOne
    @JoinColumn(name="EMPLOYEE_ID", nullable=false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROVINCE_ID")
    private Province province;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DISTRICT_ID")
    private District district;

    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HEALTH_FACILITY_ID")
    private HealthFacility healthFacility;

    @Column(name = "LOCATION_LEVEL", nullable = false, length = 80)
    private String locationLevel;


    @Creator
    public Location(){}

    public Location(LocationDTO locationDTO, Employee employee) {
        super(locationDTO);
        this.setEmployee(employee);
        this.setLocationLevel(locationDTO.getLocationLevel());
        if(locationDTO.getProvinceDTO()!=null) this.setProvince(new Province(locationDTO.getProvinceDTO()));
        if(locationDTO.getDistrictDTO()!=null)
        this.setDistrict(new District(locationDTO.getDistrictDTO()));
        if(locationDTO.getHealthFacilityDTO()!=null)
        this.setHealthFacility(new HealthFacility(locationDTO.getHealthFacilityDTO()));
    }


    private void determineLocationLevel() {
        if (this.healthFacility != null) {
            this.locationLevel = LOCATION_LEVEL_HEALTH_FACILITY;
        } else if (this.district != null) {
            this.locationLevel = LOCATION_LEVEL_DISTRITAL;
        } else if (this.province != null) {
            this.locationLevel = LOCATION_LEVEL_PROVINCIAL;
        } else {
            this.locationLevel = LOCATION_LEVEL_NATIONAL;
        }
    }

    private boolean isHealthFacilityLevel() {
        return this.locationLevel.equals(LOCATION_LEVEL_HEALTH_FACILITY);
    }

    private boolean isProvincialLevel() {
        return this.locationLevel.equals(LOCATION_LEVEL_PROVINCIAL);
    }

    private boolean isDistrictalLevel() {
        return this.locationLevel.equals(LOCATION_LEVEL_DISTRITAL);
    }

    @Override
    public String toString() {
        return "Location{" +
                ", province=" + province +
                ", district=" + district +
                ", healthFacility=" + healthFacility +
                ", locationLevel='" + locationLevel + '\'' +
                '}';
    }
}
