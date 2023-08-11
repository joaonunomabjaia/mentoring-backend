package mz.org.fgh.mentoring.entity.partnersetting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.setting.Setting;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Schema(name = "partnerSetting", description = "Represent the partner specific settings")
@Entity(name = "partnerSetting")
@Table(name = "PARTNER_SETTINGS")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
public class PartnerSetting extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTNER_ID", nullable = false)
    private Partner partner;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SETTING_ID", nullable = false)
    private Setting setting;

    @NotNull
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;

}
