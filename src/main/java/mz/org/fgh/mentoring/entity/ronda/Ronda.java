package mz.org.fgh.mentoring.entity.ronda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaMenteeDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaMentorDTO;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.entity.tutor.Tutor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Set;
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
    private Set<RondaMentee> rondaMentees;

    @OneToMany(mappedBy="ronda", fetch = FetchType.LAZY)
    private Set<Session> sessions;

    @OneToMany(mappedBy="ronda", fetch = FetchType.LAZY)
    private Set<RondaMentor> rondaMentors;

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
        this.setMentorType(rondaDTO.getMentorType());

        if (rondaDTO.getRondaType() != null) {
            this.setRondaType(new RondaType(rondaDTO.getRondaType()));
        }

        if (rondaDTO.getHealthFacility() != null) {
            this.setHealthFacility(new HealthFacility(rondaDTO.getHealthFacility()));
        }

        // Convert RondaMentors properly
        if (rondaDTO.getRondaMentors() != null && !rondaDTO.getRondaMentors().isEmpty()) {
            Set<RondaMentor> rondaMentors = rondaDTO.getRondaMentors()
                    .stream()
                    .map(RondaMentorDTO::getRondaMentor) // Ensure RondaMentorDTO has getRondaMentor() method
                    .collect(Collectors.toSet()); // Convert to Set

            this.setRondaMentors(rondaMentors);
        }


        // Convert RondaMentees properly
        if (rondaDTO.getRondaMentees() != null && !rondaDTO.getRondaMentees().isEmpty()) {
            Set<RondaMentee> rondaMentees = rondaDTO.getRondaMentees()
                    .stream()
                    .map(RondaMenteeDTO::getRondaMentee) // Ensure RondaMenteeDTO has getRondaMentee() method
                    .collect(Collectors.toSet()); // Convert to Set

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

    @JsonIgnore
    public boolean isRondaZero() {
        return this.rondaType.getCode().equals("SESSAO_ZERO");
    }

    @JsonIgnore
    public Tutor getActiveMentor() {
        if(this.getRondaMentors() == null) return null;
        for (RondaMentor rondaMentor : rondaMentors) {
            if (rondaMentor.isActive()) {
                return rondaMentor.getMentor();
            }
        }
        return null;
    }
}
