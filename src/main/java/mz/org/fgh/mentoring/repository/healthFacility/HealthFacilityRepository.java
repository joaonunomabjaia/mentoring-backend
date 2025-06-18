package mz.org.fgh.mentoring.repository.healthFacility;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthFacilityRepository extends JpaRepository<HealthFacility, Long> {

    @Override
    List<HealthFacility> findAll();


    List<HealthFacility> findByDistrictId(@NotNull Long districtId);
    @Override
    Optional<HealthFacility> findById(@NotNull Long id);

    Optional<HealthFacility> findByUuid(String uuid);

    @Query(value = "SELECT hf FROM HealthFacility hf INNER JOIN hf.district d WHERE hf.lifeCycleStatus = 'ACTIVE' and d.uuid in (:uuidList) ORDER BY d.description ",
            countQuery = "SELECT COUNT(hf) FROM HealthFacility hf INNER JOIN hf.district d WHERE hf.lifeCycleStatus = 'ACTIVE' and d.uuid in (:uuidList) ORDER BY d.description ")
    List<HealthFacility> getAllOfDistrict(List<String> uuidList);

    @Query(value = "select * from health_facilities limit :limit offset :offset ", nativeQuery = true)
    List<HealthFacility> findHealthFacilitiesWithLimit(Long limit, Long offset);

    @Query(value = "select * from health_facilities where LIFE_CYCLE_STATUS = 'ACTIVE' ", nativeQuery = true)
    List<HealthFacility> findHealthFacilitiesByPage(Pageable pageable);

    Page<HealthFacility> findByHealthFacilityIlike(String s, Pageable pageable);
}
