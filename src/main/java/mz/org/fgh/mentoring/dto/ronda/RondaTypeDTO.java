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

    private String code;

    private String description;
    public RondaTypeDTO(RondaType rondaType) {
        this.code = rondaType.getCode();
        this.description = rondaType.getDescription();
    }
}
