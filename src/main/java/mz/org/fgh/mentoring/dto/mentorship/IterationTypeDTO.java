package mz.org.fgh.mentoring.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IterationTypeDTO extends BaseEntityDTO implements Serializable {

    private String description;

    private  String code;

    public IterationTypeDTO(IterationType iterationType) {
        super(iterationType);
        this.setCode(iterationType.getCode());
        this.setDescription(iterationType.getDescription());
    }
}
