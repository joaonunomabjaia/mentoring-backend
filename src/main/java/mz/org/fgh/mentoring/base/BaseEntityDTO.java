package mz.org.fgh.mentoring.base;

import io.micronaut.core.annotation.Creator;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.api.RestAPIResponse;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BaseEntityDTO implements Serializable, RestAPIResponse {

    private Long id;

    private String uuid;

    private String lifeCycleStatus;

    private String syncStatus;

    @Creator
    public BaseEntityDTO() {
    }

    public BaseEntityDTO(BaseEntity baseEntity) {
        this.setId(baseEntity.getId());
        this.setUuid(baseEntity.getUuid());
        if(baseEntity.getLifeCycleStatus() != null) this.setLifeCycleStatus(baseEntity.getLifeCycleStatus().toString());
    }
}
