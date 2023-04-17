package mz.org.fgh.mentoring.repository.indicator;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.indicator.Indicator;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface IndicatorRepository extends CrudRepository<Indicator, Long> {

    @Override
    List<Indicator> findAll();

    @Override
    Optional<Indicator> findById(@NotNull Long id);
}
