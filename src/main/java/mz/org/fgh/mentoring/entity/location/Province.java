package mz.org.fgh.mentoring.entity.location;

import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity(name = "Province")
@Table(name = "provinces")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Province extends BaseEntity {

    @NotEmpty
    @Column(name = "DESIGNATION", unique = true, nullable = false, length = 50)
    private String designation;


}
