package mz.org.fgh.mentoring.entity.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.setting.SettingDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Schema(name = "settings", description = "Represent the system settings")
@Entity(name = "settings")
@Table(name = "SETTINGS")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class Setting extends BaseEntity {

    public static final String AI_SESSION_SUMMARY_GENERATION = "AI_SESSION_SUMMARY_GENERATION";

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
    private Boolean enabled;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    public Setting() {

    }

    public Setting(SettingDTO settingDTO) {
        super(settingDTO);
        this.setDescription(settingDTO.getDescription());
        this.setDesignation(settingDTO.getDesignation());
        this.setValue(settingDTO.getValue());
        this.setEnabled(settingDTO.getEnabled());
        this.setType(settingDTO.getType());
    }
}
