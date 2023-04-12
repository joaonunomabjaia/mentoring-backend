package mz.org.fgh.mentoring.entity.answer;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NumericAnswer extends Answer{

    @Column(name = "NUMERIC_VALUE")
    private Integer numericValue;

    @Override
    public void setValue(String value) {
        if (value == null) {
            return;
        }
        this.numericValue = Integer.valueOf(value);
    }

    @Override
    public String getValue() {
        return String.valueOf(this.numericValue);
    }
}
