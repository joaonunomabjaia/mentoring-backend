package mz.org.fgh.mentoring.entity.career;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.tutor.Tutor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "career")
@Table(name = "careers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Career extends BaseEntity {

    @NotNull
    @Column(name = "CARRER_TYPE", nullable = false, length = 50  )
    @Enumerated(EnumType.STRING)
    private CareerType careerType;

    @NotEmpty
    @Column(name = "POSITION", nullable = false)
    private String position;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "career" )
    private final Set<Tutor> tutors = new HashSet<>();
}
