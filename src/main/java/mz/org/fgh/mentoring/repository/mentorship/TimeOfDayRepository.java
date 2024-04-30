package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.mentorship.TimeOfDay;

import java.util.List;

@Repository
public interface TimeOfDayRepository extends CrudRepository<TimeOfDay, Long> {

    List<TimeOfDay> findAll();
    TimeOfDay getByCode(String code);
}
