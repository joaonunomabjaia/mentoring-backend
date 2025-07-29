package mz.org.fgh.mentoring.entity.tutored;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.session.SessionRecommendedResource;

import javax.persistence.*;
import java.util.List;

@Schema(name = "Tutoreds", description = "A professional that provides mentoring to tutored individuals")
@Entity(name = "Tutored")
@Table(name = "tutoreds")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Tutored extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @Column(name = "ZERO_EVALUATION_SCORE")
    private Double zeroEvaluationScore;

    @OneToMany(mappedBy = "tutored")
    private List<SessionRecommendedResource> recommendedResources;

    @Creator
    public Tutored() {}

    public Tutored(String uuid) {
        super(uuid);
    }

    public Tutored(TutoredDTO tutoredDTO) {
        super(tutoredDTO);
        this.setZeroEvaluationScore(tutoredDTO.getZeroEvaluationScore());
        this.setEmployee(new Employee(tutoredDTO.getEmployeeDTO()));
    }

    @Transient
    public boolean isZeroEvaluationDone() {
        return this.zeroEvaluationScore != null && this.zeroEvaluationScore > 0;
    }

    @Override
    public String toString() {
        return "Tutored{" +
                "employee=" + employee +
                '}';
    }
}
