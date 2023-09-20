package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


@Repository
public interface TutorRepository extends CrudRepository<Tutor, Long> {

    @Override
    List<Tutor> findAll();

    @Override
    Optional<Tutor> findById(@NotNull Long id);

    Optional<Tutor> findByUuid(String uuid);

    Tutor findByUser(User user);

    @Query(value = "select * from tutors limit :lim offset :of ", nativeQuery = true)
    List<Tutor> findTutorWithLimit(long lim, long of);

}
