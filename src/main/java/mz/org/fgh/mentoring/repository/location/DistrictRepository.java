package mz.org.fgh.mentoring.repository.location;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.location.District;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends CrudRepository<District, Long> {

    @Override
    List<District> findAll();

    @Override
    Optional<District> findById(@NotNull Long id);

    List<District> findByDistrict(@NotEmpty String district);
}
