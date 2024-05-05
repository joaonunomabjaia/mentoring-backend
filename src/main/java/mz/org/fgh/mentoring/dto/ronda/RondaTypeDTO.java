package mz.org.fgh.mentoring.dto.ronda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.ronda.RondaType;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RondaTypeDTO implements Serializable {

    private Long id;

    private String uuid;

    private String code;

    private String description;

    public RondaTypeDTO(RondaType rondaType) {
        this.id = rondaType.getId();
        this.uuid = rondaType.getUuid();
        this.code = rondaType.getCode();
        this.description = rondaType.getDescription();
    }

    public RondaType getRondaType() {
        RondaType rondaType = new RondaType();
        rondaType.setId(this.getId());
        rondaType.setUuid(this.getUuid());
        rondaType.setCode(this.getCode());
        rondaType.setDescription(this.getDescription());
        return rondaType;
    }
}
