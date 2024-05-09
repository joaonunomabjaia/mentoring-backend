package mz.org.fgh.mentoring.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.question.ResponseType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTypeDTO extends BaseEntityDTO implements Serializable {

    private String description;

    private  String code;

    public ResponseTypeDTO(ResponseType responseType) {
        super(responseType);
        this.description = responseType.getDescription();
        this.code = responseType.getCode();
    }

    public ResponseType toResponseType() {
        ResponseType responseType = new ResponseType();
        responseType.setCode(this.getCode());
        responseType.setId(this.getId());
        responseType.setDescription(this.getDescription());
        responseType.setUuid(this.getUuid());
        if (Utilities.stringHasValue(this.getLifeCycleStatus())) responseType.setLifeCycleStatus(LifeCycleStatus.valueOf(this.getLifeCycleStatus()));
        return responseType;
    }
}
