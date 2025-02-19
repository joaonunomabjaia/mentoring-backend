package mz.org.fgh.mentoring.dto.ronda;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RondaDTO extends BaseEntityDTO {

    private String description;

    private Date startDate;

    private Date endDate;

    private String mentorType;

    @JsonProperty(value = "rondaTypeDTO")
    private RondaTypeDTO rondaType;

    private HealthFacilityDTO healthFacility;

    private Set<RondaMenteeDTO> rondaMentees;

    private Set<RondaMentorDTO> rondaMentors;

    public RondaDTO() {
    }

    public RondaDTO(Ronda ronda) {
        super(ronda);
        this.setDescription(ronda.getDescription());
        this.setStartDate(ronda.getStartDate());
        this.setMentorType(ronda.getMentorType());
        this.setEndDate(ronda.getEndDate());
        if(ronda.getEndDate()!=null) { this.setEndDate(ronda.getEndDate()); }
        if(ronda.getRondaType()!=null) { this.setRondaType(new RondaTypeDTO(ronda.getRondaType())); }
        if(ronda.getHealthFacility()!=null) this.setHealthFacility(new HealthFacilityDTO(ronda.getHealthFacility()));
        if (Utilities.hasElements(ronda.getRondaMentees())) {
            this.setRondaMentees(ronda.getRondaMentees()
                    .stream()
                    .map(RondaMenteeDTO::new)
                    .collect(Collectors.toSet()) // Use Set instead of List to prevent MultipleBagFetchException
            );
        }

        if (Utilities.hasElements(ronda.getRondaMentors())) {
            this.setRondaMentors(ronda.getRondaMentors()
                    .stream()
                    .map(RondaMentorDTO::new)
                    .collect(Collectors.toSet()) // Use Set instead of List
            );
        }
    }

    @JsonIgnore
    public Ronda getRonda() {
        Ronda ronda = new Ronda();
        ronda.setId(this.getId());
        ronda.setUuid(this.getUuid());
        ronda.setMentorType(this.getMentorType());
        ronda.setDescription(this.getDescription());
        ronda.setStartDate(this.getStartDate());
        ronda.setEndDate(this.getEndDate());
        ronda.setCreatedAt(this.getCreatedAt());
        ronda.setUpdatedAt(this.getUpdatedAt());

        if (Utilities.stringHasValue(this.getLifeCycleStatus())) {
            ronda.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        }

        if (this.getHealthFacility() != null) {
            ronda.setHealthFacility(this.getHealthFacility().getHealthFacilityObj());
        }

        if (this.getRondaType() != null) {
            ronda.setRondaType(this.getRondaType().getRondaType());
        }

        // Convert RondaMentors using Streams
        if (Utilities.hasElements(this.getRondaMentors())) {
            ronda.setRondaMentors(this.getRondaMentors()
                    .stream()
                    .map(RondaMentorDTO::getRondaMentor)
                    .collect(Collectors.toSet()) // Use Set instead of List to avoid Hibernate MultipleBagFetchException
            );
        }

        // Convert RondaMentees using Streams
        if (Utilities.hasElements(this.getRondaMentees())) {
            ronda.setRondaMentees(this.getRondaMentees()
                    .stream()
                    .map(RondaMenteeDTO::getRondaMentee)
                    .collect(Collectors.toSet()) // Use Set instead of List
            );
        }

        return ronda;
    }

}
