package mz.org.fgh.mentoring.entity.career;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity(name = "Career")
@Table(name = "CARRERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Career extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CARRER_TYPE_ID")
    private CareerType careerType;

    @NotEmpty
    @Column(name = "POSITION", nullable = false)
    private String position;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "career" )
    private final Set<Tutor> tutors = new HashSet<>();
}
