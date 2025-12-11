package mz.org.fgh.mentoring.repository.settings;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface SettingsRepository extends CrudRepository<Setting, Long> {

    @Override
    List<Setting> findAll();

    @Override
    Optional<Setting> findById(@NotNull Long id);

    Optional<Setting> findByUuidAndLifeCycleStatus(final String uuid, final LifeCycleStatus lifeCycleStatus);

    @Query(value = "select * from settings limit :lim offset :of ", nativeQuery = true)
    List<Setting> findSettingWithLimit(long lim, long of);

    Optional<Setting> findByDesignation(@NotNull String designation);

    Page<Setting> findByDesignationIlike(String designation, Pageable pageable);

    Optional<Setting> findByDesignationAndEnabledTrueAndLifeCycleStatusNotEquals(
            String designation, LifeCycleStatus lifeCycleStatus);
}
