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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Schema(name = "Tutoreds", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "Tutored")
@Table(name = "tutoreds")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Tutored extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @Transient
    private boolean zeroEvaluationDone;

    @Column(name = "ZERO_EVALUATION_SCORE")
    private Double zeroEvaluationScore;

    @OneToMany(mappedBy = "tutored", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionRecommendedResource> recommendedResources;

    @Creator
    public Tutored() {

    }

    public Tutored(TutoredDTO tutoredDTO) {
        super(tutoredDTO);
        this.setEmployee(new Employee(tutoredDTO.getEmployeeDTO()));
    }

    @Override
    public String toString() {
        return "Tutored{" +
                "employee=" + employee +
                '}';
    }
}
