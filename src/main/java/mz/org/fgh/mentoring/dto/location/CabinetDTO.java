package mz.org.fgh.mentoring.dto.location;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;

import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
public class CabinetDTO implements Serializable {

    private String uuid;

    private String name;

    public CabinetDTO(final Cabinet cabinet){
        this.setUuid(cabinet.getUuid());
        this.setName(cabinet.getName());
    }
}
