package mz.org.fgh.mentoring.entity.ronda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.tutor.Tutor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "RondaMentor")
@Table(name = "ronda_mentor")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RondaMentor extends BaseEntity {

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "RONDA_ID")
      private Ronda ronda;

      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name = "MENTOR_ID")
      private Tutor mentor;

      @Column(name = "START_DATE", nullable = false)
      private Date startDate;

      @Column(name = "END_DATE")
      private Date endDate;
}
