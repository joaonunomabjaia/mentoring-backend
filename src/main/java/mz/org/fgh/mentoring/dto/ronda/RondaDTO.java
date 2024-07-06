package mz.org.fgh.mentoring.dto.ronda;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RondaDTO extends BaseEntityDTO {

    private String description;

    private Date startDate;

    private Date endDate;

    @JsonProperty(value = "rondaTypeDTO")
    private RondaTypeDTO rondaType;

    private HealthFacilityDTO healthFacility;

    private List<RondaMenteeDTO> rondaMentees;

    private List<RondaMentorDTO> rondaMentors;

    public RondaDTO() {
    }

    public RondaDTO(Ronda ronda) {
        super(ronda);
        this.setDescription(ronda.getDescription());
        this.setStartDate(ronda.getStartDate());
        this.setEndDate(ronda.getEndDate());
        if(ronda.getEndDate()!=null) {
            this.setEndDate(ronda.getEndDate());
        }
        if(ronda.getRondaType()!=null) {
            this.setRondaType(new RondaTypeDTO(ronda.getRondaType()));
        }
        this.setHealthFacility(new HealthFacilityDTO(ronda.getHealthFacility()));
        if (Utilities.listHasElements(ronda.getRondaMentees())) {
            List<RondaMenteeDTO> rondaMenteeDTOS = ronda.getRondaMentees().stream()
                    .map(RondaMenteeDTO::new)
                    .collect(Collectors.toList());
            this.setRondaMentees(rondaMenteeDTOS);
        }
        if (Utilities.listHasElements(ronda.getRondaMentors())) {
            List<RondaMentorDTO> rondaMentorDTOS = ronda.getRondaMentors().stream()
                    .map(RondaMentorDTO::new)
                    .collect(Collectors.toList());
            this.setRondaMentors(rondaMentorDTOS);
        }
    }

    public Ronda getRonda() {
        Ronda ronda = new Ronda();
        ronda.setId(this.getId());
        ronda.setUuid(this.getUuid());
        ronda.setDescription(this.getDescription());
        ronda.setStartDate(this.getStartDate());
        ronda.setEndDate(this.getEndDate());
        ronda.setCreatedAt(this.getCreatedAt());
        ronda.setUpdatedAt(this.getUpdatedAt());
        ronda.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        if(this.getHealthFacility()!=null) {
            ronda.setHealthFacility(this.getHealthFacility().getHealthFacilityObj());
        }
        if(this.getRondaType()!=null) {
            ronda.setRondaType(this.getRondaType().getRondaType());
        }
        if(Utilities.listHasElements(this.getRondaMentors())) {
            List<RondaMentor> rondaMentors = new ArrayList<>();
            for (RondaMentorDTO rondaMentorDTO: this.getRondaMentors()) {
                RondaMentor rondaMentor = rondaMentorDTO.getRondaMentor();
                rondaMentors.add(rondaMentor);
            }
            ronda.setRondaMentors(rondaMentors);
        }
        if(Utilities.listHasElements(this.getRondaMentees())) {
            List<RondaMentee> rondaMentees = new ArrayList<>();
            for (RondaMenteeDTO rondaMenteeDTO: this.getRondaMentees()) {
                RondaMentee rondaMentee = rondaMenteeDTO.getRondaMentee();
                rondaMentees.add(rondaMentee);
            }
            ronda.setRondaMentees(rondaMentees);
        }
        return ronda;
    }
}
