package mz.org.fgh.mentoring.repository.settings;

import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.partnersetting.PartnerSetting;

public interface PartnerSettingRepository extends CrudRepository<PartnerSetting, Long> {
}
