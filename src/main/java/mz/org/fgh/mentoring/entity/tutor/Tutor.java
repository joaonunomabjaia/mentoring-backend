package mz.org.fgh.mentoring.entity.tutor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.util.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "tutor")
@Table(name = "tutors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tutor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "ID",nullable = false)
    private Long id;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tutor tutor = (Tutor) o;
        return id.equals(tutor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
