package mz.org.fgh.mentoring.entity.employee;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Schema(name = "Employee", description = "A professional that works on an health facility")
@Entity(name = "Employee")
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Employee extends BaseEntity {

    @NotEmpty
    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @NotEmpty
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @NotEmpty
    @Column(name = "SURNAME", nullable = false, length = 50)
    private String surname;

    @NotEmpty
    @Column(name = "NUIT", nullable = false, length = 9)
    private int nuit;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID")
    private ProfessionalCategory professionalCategory;

    @NotEmpty
    @Column(name = "TRAINING_YEAR", length = 4)
    private int trainingYear;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "EMAIL", nullable = false, length = 50)
    @Email
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTNER_ID")
    private Partner partner;

}
