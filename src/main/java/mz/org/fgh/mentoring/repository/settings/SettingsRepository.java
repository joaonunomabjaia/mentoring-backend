package mz.org.fgh.mentoring.repository.settings;

import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.setting.Setting;

public interface SettingsRepository extends CrudRepository<Setting, Long> {
}
