package mz.org.fgh.mentoring.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
public class CabinetDTO extends BaseEntityDTO {

    private String name;

    public CabinetDTO(final Cabinet cabinet){
        super(cabinet);
        this.setName(cabinet.getName());
    }

    public Cabinet toEntity(){
        Cabinet cabinet = new Cabinet();
        cabinet.setUuid(this.getUuid());
        cabinet.setId(this.getId());
        cabinet.setCreatedAt(this.getCreatedAt());
        cabinet.setUpdatedAt(this.getUpdatedAt());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) cabinet.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        cabinet.setName(this.getName());
        return cabinet;
    }
}
