package mz.org.fgh.mentoring.repository.settings;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.partnersetting.PartnerSetting;
import mz.org.fgh.mentoring.entity.setting.Setting;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerSettingRepository extends CrudRepository<PartnerSetting, Long> {
    List<PartnerSetting> findByPartner(@NotNull Partner partner);
}
