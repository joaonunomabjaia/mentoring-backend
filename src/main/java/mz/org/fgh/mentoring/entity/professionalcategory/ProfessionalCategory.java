package mz.org.fgh.mentoring.entity.professionalcategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Schema(name = "ProfessionalCategory", description = "Professional Category of an Employee")
@Entity
@Table(name = "professional_category")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class ProfessionalCategory extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;
}
