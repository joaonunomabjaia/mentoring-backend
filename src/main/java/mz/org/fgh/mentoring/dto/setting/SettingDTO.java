package mz.org.fgh.mentoring.dto.setting;

import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.setting.Setting;

@Data
public class SettingDTO extends BaseEntityDTO {

    private String designation;

    private String value;

    private String description;

    private String type;

    private Boolean enabled;

    public SettingDTO() {
    }

    public SettingDTO(final Setting setting) {
        super(setting);
        this.setDesignation(setting.getDesignation());
        this.setDescription(setting.getDescription());
        this.setValue(setting.getValue());
        this.setType(setting.getType());
        this.setEnabled(setting.getEnabled());
    }

}
