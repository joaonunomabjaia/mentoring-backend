package mz.org.fgh.mentoring.entity.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author Jose Julai Ritsure
 */
@Entity
@Table(name = "evaluation_type")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class EvaluationType extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    public EvaluationType() {

    }

    public EvaluationType(EvaluationTypeDTO evaluationTypeDTO) {
        super(evaluationTypeDTO);
        this.setCode(evaluationTypeDTO.getCode());
        this.setDescription(evaluationTypeDTO.getDescription());
    }
}
