package mz.org.fgh.mentoring.dto.ronda;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RondaDTO extends BaseEntityDTO {


    private String code;

    private String description;

    private LocalDateTime dateBegin;

    @JsonProperty(value = "rondaTypeDTO")
    private RondaTypeDTO rondaTypeDTO;

    public RondaDTO() {
    }

    public RondaDTO(Ronda ronda) {
        super(ronda);
        this.code = ronda.getCode();
        this.description = ronda.getDescription();
        this.dateBegin = ronda.getDateBegin();
        this.rondaTypeDTO = new RondaTypeDTO(ronda.getRondaType());
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
        return ronda;
    }
}
