package mz.org.fgh.mentoring.dto.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.ResponseType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResponseTypeDTO extends BaseEntityDTO {
    private String code;
    private String description;
    public ResponseTypeDTO(ResponseType responseType) {
        super(responseType);
        this.setCode(responseType.getCode());
        this.setDescription(responseType.getDescription());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ResponseType getResponseType() {
        ResponseType responseType = new ResponseType();
        responseType.setUuid(this.getUuid());
        responseType.setId(this.getId());
        responseType.setCreatedAt(this.getCreatedAt());
        responseType.setUpdatedAt(this.getUpdatedAt());
        responseType.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        responseType.setCode(responseType.getCode());
        responseType.setDescription(responseType.getDescription());
        return responseType;
    }
}
