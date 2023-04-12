package mz.org.fgh.mentoring.entity.indicator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.location.HealthFacility;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(name = "Indicator", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "indicator")
@Table(name = "indicators")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Indicator extends BaseEntity {

    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Column(name = "PERFORMED_DATE", nullable = false)
    private LocalDateTime performedDate;

    @Column(name = "REFERRED_MONTH", nullable = false)
    private LocalDate referredMonth;

    @JoinColumn(name = "TUTOR_ID", nullable = false)
    private Tutor tutor;

    @JoinColumn(name = "FORM_ID", nullable = false)
    private Form form;

    @JoinColumn(name = "HEALTH_FACILITY_ID", nullable = false)
    private HealthFacility healthFacility;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Indicator indicator = (Indicator) o;
        return code.equals(indicator.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
