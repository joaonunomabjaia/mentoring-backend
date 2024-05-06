package mz.org.fgh.mentoring.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.util.SyncStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntityDTO implements Serializable, RestAPIResponse {

    private Long id;

    private String uuid;

    private String lifeCycleStatus;

    private String syncStatus;

    public BaseEntityDTO(BaseEntity baseEntity) {
        this.setId(baseEntity.getId());
        this.setUuid(baseEntity.getUuid());
        if(baseEntity.getLifeCycleStatus() != null) this.setLifeCycleStatus(baseEntity.getLifeCycleStatus().toString());
        if(baseEntity.getSyncStatus() != null) this.setSyncStatus(baseEntity.getSyncStatus().toString());
    }
}
