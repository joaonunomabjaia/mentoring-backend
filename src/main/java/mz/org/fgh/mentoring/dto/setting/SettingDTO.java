package mz.org.fgh.mentoring.dto.setting;

import lombok.Data;
import mz.org.fgh.mentoring.entity.setting.Setting;

import java.io.Serializable;

@Data
public class SettingDTO implements Serializable {

    private String uuid;

    private String designation;

    private String value;

    private String description;

    private String type;

    private Boolean enabled;

    public SettingDTO() {
    }

    public SettingDTO(String designation, String value, String description, String type, Boolean enabled) {
        this.setDesignation(designation);
        this.setDescription(description);
        this.setValue(value);
        this.setType(type);
        this.setEnabled(enabled);
    }

    public SettingDTO(String uuid, String designation, String value, String description, String type, Boolean enabled) {
        this.setUuid(uuid);
        this.setDesignation(designation);
        this.setDescription(description);
        this.setValue(value);
        this.setType(type);
        this.setEnabled(enabled);
    }

    public SettingDTO(final Setting setting) {
        this.setUuid(setting.getUuid());
        this.setDesignation(setting.getDesignation());
        this.setDescription(setting.getDescription());
        this.setValue(setting.getValue());
        this.setType(setting.getType());
        this.setEnabled(setting.getEnabled());
    }

}
