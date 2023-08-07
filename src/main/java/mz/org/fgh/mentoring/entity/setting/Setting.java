package mz.org.fgh.mentoring.entity.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;


@Schema(name = "settings", description = "Represent the system settings")
@Entity(name = "settings")
@Table(name = "SETTINGS")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Setting extends BaseEntity {

    public static final String SETTING_TYPE_DATE = "DATE";
    public static final String SETTING_TYPE_TEXT = "TEXT";
    public static final String SETTING_TYPE_NUMERIC = "NUMERIC";

    @NotNull
    @Column(name = "DESIGNATION", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "SETTING_VALUE", nullable = false)
    private String value;

    @NotNull
    @Column(name = "SETTING_TYPE", nullable = false)
    private String type;

    @NotNull
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;
}
