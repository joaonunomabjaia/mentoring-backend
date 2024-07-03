package mz.org.fgh.mentoring.entity.ronda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import mz.org.fgh.mentoring.util.Utilities;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "Ronda")
@Table(name = "rondas")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class Ronda extends BaseEntity {

    @NotEmpty
    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RONDA_TYPE_ID")
    private RondaType rondaType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HEALTH_FACILITY_ID")
    private HealthFacility healthFacility;

    @OneToMany(mappedBy="ronda", fetch = FetchType.LAZY)
    private List<RondaMentee> rondaMentees;

    @OneToMany(mappedBy="ronda", fetch = FetchType.LAZY)
    private List<RondaMentor> rondaMentors;

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    public Ronda () {
    }

    public Ronda(RondaDTO rondaDTO) {
        super(rondaDTO);
        this.setDescription(rondaDTO.getDescription());
        this.setStartDate(rondaDTO.getStartDate());
        this.setEndDate(rondaDTO.getEndDate());
        this.setRondaType(new RondaType(rondaDTO.getRondaType()));
        if(rondaDTO.getHealthFacility()!=null) {
            this.setHealthFacility(new HealthFacility(rondaDTO.getHealthFacility()));
        }
        if (Utilities.listHasElements(rondaDTO.getRondaMentors())) {
            List<RondaMentor> rondaMentors = rondaDTO.getRondaMentors().stream()
                    .map(RondaMentor::new)
                    .collect(Collectors.toList());
            this.setRondaMentors(rondaMentors);
        }

        if (Utilities.listHasElements(rondaDTO.getRondaMentees())) {
            List<RondaMentee> rondaMentees = rondaDTO.getRondaMentees().stream()
                    .map(RondaMentee::new)
                    .collect(Collectors.toList());
            this.setRondaMentees(rondaMentees);
        }
    }

    @Override
    public String toString() {
        return "Ronda{" +
                "description='" + description + '\'' +
                ", rondaType=" + rondaType +
                ", healthFacility=" + healthFacility +
                ", rondaMentees=" + rondaMentees +
                ", rondaMentors=" + rondaMentors +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

}
