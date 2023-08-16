package mz.org.fgh.mentoring.repository.location;

import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.location.Province;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Jose Julai Ritsure
 */
public interface ProvinceRepository extends CrudRepository<Province, Long> {

    @Override
    List<Province> findAll();

    @Override
    Optional<Province> findById(@NotNull Long id);

    List<Province> findByDesignation(@NotEmpty String designation);
}
