package mz.org.fgh.mentoring.entity.answer;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TextAnswer extends Answer{

    public static final String NAME = "TEXT";

    @Column(name = "TEXT_VALUE", length = 180)
    private String textValue;

    @Override
    public void setValue(final String value) {
        this.textValue = value;
    }

    @Override
    public String getValue() {
        return this.textValue;
    }
}
