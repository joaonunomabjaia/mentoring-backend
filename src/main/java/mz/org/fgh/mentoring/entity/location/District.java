package mz.org.fgh.mentoring.entity.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "districts")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class District extends BaseEntity {

    @NotNull
    @Column(name = "PROVINCE", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Province province;

    @NotEmpty
    @Column(name = "DISTRICT", nullable = false, length = 50)
    private String district;
}
