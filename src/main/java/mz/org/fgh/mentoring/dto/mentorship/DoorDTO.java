package mz.org.fgh.mentoring.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.mentorship.Door;
import mz.org.fgh.mentoring.entity.mentorship.TimeOfDay;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoorDTO extends BaseEntityDTO {

    private String description;

    private  String code;

    public DoorDTO(Door door) {
        super(door);
        this.description = door.getDescription();
        this.code = door.getCode();
    }

    public Door toDoor() {
        Door door = new Door();
        door.setCode(this.getCode());
        door.setId(this.getId());
        door.setDescription(this.getDescription());
        door.setUuid(this.getUuid());
        door.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        return door;
    }
}
