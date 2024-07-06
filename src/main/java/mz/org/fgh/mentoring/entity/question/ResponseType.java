package mz.org.fgh.mentoring.entity.question;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.form.ResponseTypeDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "response_type")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class ResponseType extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    public ResponseType() {

    }

    public ResponseType(ResponseTypeDTO responseTypeDTO) {
        super(responseTypeDTO);
        this.setDescription(responseTypeDTO.getDescription());
        this.setCode(responseTypeDTO.getCode());
    }

}
