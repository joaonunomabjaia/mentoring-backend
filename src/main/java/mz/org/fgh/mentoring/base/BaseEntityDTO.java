package mz.org.fgh.mentoring.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.api.RestAPIResponse;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntityDTO implements Serializable, RestAPIResponse {

    private Long id;

    private String uuid;

    private String lifeCycleStatus;

    public BaseEntityDTO(BaseEntity baseEntity) {
        this.setId(baseEntity.getId());
        this.setUuid(baseEntity.getUuid());
        this.setLifeCycleStatus(baseEntity.getLifeCycleStatus().toString());
    }
}
