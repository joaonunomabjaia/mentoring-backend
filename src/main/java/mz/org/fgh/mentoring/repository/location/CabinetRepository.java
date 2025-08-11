package mz.org.fgh.mentoring.repository.location;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;

import java.util.List;
import java.util.Optional;

@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {

    Optional<Cabinet> findByUuid(String uuid);

    @Query(value = "select * from cabinets limit :lim offset :of ", nativeQuery = true)
    List<Cabinet> findCabinetWithLimit(long lim, long of);

    Page<Cabinet> findByNameIlike(String name, Pageable pageable);
}
