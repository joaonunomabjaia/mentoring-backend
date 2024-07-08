package mz.org.fgh.mentoring.entity.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.mentorship.TimeOfDayDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author Jose Julai Ritsure
 */
@Entity
@Table(name = "times_of_day")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class TimeOfDay extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    public TimeOfDay() {

    }

    public TimeOfDay(TimeOfDayDTO timeOfDayDTO) {
        super(timeOfDayDTO);
        this.setCode(timeOfDayDTO.getCode());
        this.setDescription(timeOfDayDTO.getDescription());
    }
}
