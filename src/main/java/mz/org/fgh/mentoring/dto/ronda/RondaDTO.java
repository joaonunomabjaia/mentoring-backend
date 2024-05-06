package mz.org.fgh.mentoring.dto.ronda;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.ronda.Ronda;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RondaDTO implements Serializable {

    private Long id;

    private String uuid;

    private String code;

    private String description;

    private LocalDateTime dateBegin;

    @JsonProperty(value = "rondaTypeDTO")
    private RondaTypeDTO rondaTypeDTO;

    public RondaDTO(Ronda ronda) {
        this.id = ronda.getId();
        this.uuid = ronda.getUuid();
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
