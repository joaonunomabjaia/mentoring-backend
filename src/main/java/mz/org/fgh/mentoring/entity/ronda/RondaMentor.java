package mz.org.fgh.mentoring.entity.ronda;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.tutor.Tutor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "RondaMentor")
@Table(name = "rondamentor")
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

      @Column(name = "STATE")
      private Boolean state;

      private LocalDate dateBegin;
}
