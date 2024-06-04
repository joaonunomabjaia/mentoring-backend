package mz.org.fgh.mentoring.dto.ronda;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.healthFacility.HealthFacilityDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaMentee;
import mz.org.fgh.mentoring.entity.ronda.RondaMentor;
import mz.org.fgh.mentoring.util.Utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RondaDTO extends BaseEntityDTO {

    private String code;

    private String description;

    private LocalDateTime dateBegin;

    @JsonProperty(value = "rondaTypeDTO")
    private RondaTypeDTO rondaTypeDTO;

    private HealthFacilityDTO healthFacility;

    private List<RondaMenteeDTO> rondaMentees;

    private List<RondaMentorDTO> rondaMentors;

    public RondaDTO() {
    }

    public RondaDTO(Ronda ronda) {
        super(ronda);
        this.code = ronda.getCode();
        this.description = ronda.getDescription();
        this.dateBegin = ronda.getDateBegin();
        this.rondaTypeDTO = new RondaTypeDTO(ronda.getRondaType());
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
        ronda.setCode(this.getCode());
        ronda.setDescription(this.getDescription());
        ronda.setDateBegin(this.getDateBegin());
        if(this.getRondaTypeDTO()!=null) {
            ronda.setRondaType(this.getRondaTypeDTO().getRondaType());
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
