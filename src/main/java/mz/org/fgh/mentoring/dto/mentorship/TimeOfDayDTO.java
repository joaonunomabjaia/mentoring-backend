package mz.org.fgh.mentoring.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;
import mz.org.fgh.mentoring.entity.mentorship.TimeOfDay;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeOfDayDTO extends BaseEntityDTO implements Serializable {

    private String description;

    private  String code;

    public TimeOfDayDTO(TimeOfDay timeOfDay) {
        super(timeOfDay);
        this.setCode(timeOfDay.getCode());
        this.setDescription(timeOfDay.getDescription());
    }
}
