package mz.org.fgh.mentoring.repository.tutored;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import java.util.List;

@Repository
public interface TutoredRepository extends CrudRepository<Tutored, Long> {

    List<Tutored> findAll();
}
