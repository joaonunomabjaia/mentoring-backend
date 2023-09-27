package mz.org.fgh.mentoring.repository.tutored;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutoredRepository extends CrudRepository<Tutored, Long> {

    List<Tutored> findAll();

    @Query(value = "select * from tutoreds limit :limi offset :off", nativeQuery = true)
    List<Tutored> findTutoredWithLimit (long limi, long off);

    Optional<Tutored> findByUuid(String uuid);

    @Query("From Tutored td inner join fetch td.career c inner join fetch c.tutors t where t.uuid = :tutorUuid")
    List<Tutored> findTutoredByTutorUuid(String tutorUuid);
}
