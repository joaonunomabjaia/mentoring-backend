package mz.org.fgh.mentoring.entity.answer;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BooleanAnswer extends Answer{

    public static final String NAME = "BOOLEAN";
    @Column(name = "BOOLEAN_VALUE")
    private Boolean booleanValue;

    @Override
    public void setValue(String value) {
        this.booleanValue = Boolean.valueOf(value);
    }
    @Override
    public String getValue() {
        return String.valueOf(this.booleanValue);
    }
}
