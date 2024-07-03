package mz.org.fgh.mentoring.entity.ronda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.ronda.RondaMentorDTO;
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
      public RondaMentor() {
      }

      public RondaMentor(RondaMentorDTO rondaMentorDTO) {
            super(rondaMentorDTO);
            this.setStartDate(rondaMentorDTO.getStartDate());
            this.setEndDate(rondaMentorDTO.getEndDate());
            this.setMentor(new Tutor(rondaMentorDTO.getMentor()));
            this.setRonda(new Ronda());
            this.getRonda().setUuid(rondaMentorDTO.getRonda().getUuid());
      }


      @Override
      public String toString() {
            return "RondaMentor{" +
                    "ronda=" + ronda +
                    ", mentor=" + mentor +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    '}';
      }
}
