package mz.org.fgh.mentoring.entity.programaticarea;

import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Column;
import java.util.Objects;

public class ProgramaticArea extends BaseEntity {

    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramaticArea that = (ProgramaticArea) o;
        return code.equals(that.code) && name.equals(that.name) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, description);
    }
}
