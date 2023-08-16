package mz.org.fgh.mentoring.entity.location;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "District")
@Table(name = "districts")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class District extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROVINCE_ID", nullable = false)
    private Province province;

    @NotEmpty
    @Column(name = "DISTRICT", unique = true, nullable = false, length = 50)
    private String district;

}
