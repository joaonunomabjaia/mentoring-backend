package mz.org.fgh.mentoring.dto.setting;

import lombok.Data;
import mz.org.fgh.mentoring.entity.setting.Setting;

import java.io.Serializable;

@Data
public class SettingDTO implements Serializable {

    private String designation;

    private String value;

    private String description;

    private String type;

    private Boolean enabled;

    public SettingDTO() {
    }

    public SettingDTO(String designation, String value, String description, String type, Boolean enabled) {
        this.designation = designation;
        this.value = value;
        this.description = description;
        this.type = type;
        this.enabled = enabled;
    }

    public SettingDTO(final Setting setting) {
        this.designation = setting.getDesignation();
        this.value = setting.getValue();
        this.description = setting.getDescription();
        this.type = setting.getType();
        this.enabled = setting.getEnabled();
    }

}
