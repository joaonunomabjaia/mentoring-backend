package mz.org.fgh.mentoring.entity.tutored;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.entity.tutor.Tutor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Jose Julai Ritsure
 */
@Entity
@Table(name = "tutor_tutored")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TutorTutored extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTOR_ID", nullable = false)
    private Tutor tutor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTORED_ID", nullable = false)
    private Tutored tutored;
}
