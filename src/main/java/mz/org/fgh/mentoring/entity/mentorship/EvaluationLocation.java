package mz.org.fgh.mentoring.entity.mentorship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.mentorship.EvaluationLocationDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "evaluation_location")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
public class EvaluationLocation extends BaseEntity {

    public static final String HEALTH_FACILITY = "HEALTH_FACILITY";
    public static final String COMMUNITY = "COMMUNITY";

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    public EvaluationLocation() {
    }

    public EvaluationLocation(String uuid) {
        super(uuid);
    }

    public EvaluationLocation(EvaluationLocationDTO evaluationLocationDTO) {
        super(evaluationLocationDTO);
        this.setCode(evaluationLocationDTO.getCode());
        this.setDescription(evaluationLocationDTO.getDescription());
    }

    @JsonIgnore
    public boolean isComumunityEvaluation() {
        return HEALTH_FACILITY.equals(code);
    }

    @JsonIgnore
    public boolean isHealthFacilityEvaluation() {
        return HEALTH_FACILITY.equals(code);
    }
}
