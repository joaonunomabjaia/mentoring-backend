package mz.org.fgh.mentoring.repository.location;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.form.Form;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface CabinetRepository extends CrudRepository<Cabinet, Long> {

    @Override
    List<Cabinet> findAll();

    @Override
    Optional<Cabinet> findById(@NotNull Long id);

    Optional<Cabinet> findByUuid(String uuid);

    @Query(value = "select * from cabinets limit :lim offset :of ", nativeQuery = true)
    List<Cabinet> findCabinetWithLimit(long lim, long of);
}
