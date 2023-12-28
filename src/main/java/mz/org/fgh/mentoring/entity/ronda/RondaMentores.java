package mz.org.fgh.mentoring.entity.ronda;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "RondaMentores")
@Table(name = "rondamentores")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RondaMentores extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RONDA_ID")
    private Ronda ronda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MENTORES_ID")
    private Tutored tutored;

    @Column(name = "STATE")
    private String result;


}
