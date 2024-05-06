package mz.org.fgh.mentoring.entity.session;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = SessionStatus.TABLE_NAME)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class SessionStatus extends BaseEntity {

    public static final String TABLE_NAME = "session_status";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_CODE = "code";

    @NotNull
    @Column(name = COLUMN_DESCRIPTION, nullable = false)
    private String description;

    @NotNull
    @Column(name = COLUMN_CODE, nullable = false)
    private String code;

    public SessionStatus() {
    }

    public SessionStatus(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
