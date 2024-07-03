package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.mentorship.Door;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoorRepository extends CrudRepository<Door, Long> {

    List<Door> findAll();
    Door getByCode(String code);
    Optional<Door> findByUuid(String uuid);
}
