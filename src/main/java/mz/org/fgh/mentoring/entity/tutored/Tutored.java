package mz.org.fgh.mentoring.entity.tutored;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.entity.user.User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "TUTOREDS")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Tutored extends BaseEntity {

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
    @Column(name = "PHONE_NUMBER", nullable = false, length = 100)
    private String phoneNumber;

    @Column(name = "EMAIL", length = 50)
    @Email
    private String email;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARRER_ID", nullable = false)
    private Career career;

    @Version
    @Column(name = "VERSION")
    private int version;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

}
