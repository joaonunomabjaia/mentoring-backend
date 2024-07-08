package mz.org.fgh.mentoring.entity.ronda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.util.Utilities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @Column(name = "mentor_type")
    private String mentorType;

    public Ronda () {
    }

    public Ronda(RondaDTO rondaDTO) {
        super(rondaDTO);
        this.setDescription(rondaDTO.getDescription());
        this.setStartDate(rondaDTO.getStartDate());
        this.setEndDate(rondaDTO.getEndDate());
        if(rondaDTO.getRondaType()!=null) this.setRondaType(new RondaType(rondaDTO.getRondaType()));
        this.setRondaType(new RondaType(rondaDTO.getRondaType()));
        this.setMentorType(rondaDTO.getMentorType());
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
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @JsonIgnore
    public boolean isComplete() {
        return this.endDate != null;
    }
}
