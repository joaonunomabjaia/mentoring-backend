package mz.org.fgh.mentoring.entity.ronda;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "Ronda")
@Table(name = "rondas")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ronda extends BaseEntity {

    @NotEmpty
    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @NotEmpty
    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    @Column(name = "DETE_BEGIN")
    private LocalDateTime dateBegin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RONDA_TYPE")
    private RondaType rondaType;


}
