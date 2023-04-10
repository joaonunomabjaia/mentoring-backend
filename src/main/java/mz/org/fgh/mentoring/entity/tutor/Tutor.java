package mz.org.fgh.mentoring.entity.tutor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Schema(name = "Tutor", description = "A professional that provide mentoring to the tutored individuals")
@Entity(name = "tutor")
@Table(name = "tutors")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Tutor extends BaseEntity {

    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "SURNAME", nullable = false, length = 50)
    private String surname;


    @Column(name = "PHONE_NUMBER", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @Column(name = "IS_USER", nullable = false)
    private Boolean isUser = Boolean.FALSE;

}
