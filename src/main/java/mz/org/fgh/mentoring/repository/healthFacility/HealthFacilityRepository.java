package mz.org.fgh.mentoring.repository.healthFacility;

import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


public interface HealthFacilityRepository extends CrudRepository<HealthFacility, Long> {

    @Override
    List<HealthFacility> findAll();


    List<HealthFacility> findByDistrictId(@NotNull Long districtId);
    @Override
    Optional<HealthFacility> findById(@NotNull Long id);

    Optional<HealthFacility> findByUuid(String uuid);


    List<HealthFacility> getAllOfDistrict(List<String> uuids);
}
