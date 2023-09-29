package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.entity.career.CareerType;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface CareerRepository extends CrudRepository<Career, Long> {

    List<Career> findAll();

    Optional<Career> findById(@NotNull Long id);

    @Query("FROM Career c WHERE c.careerType = :c AND c.lifeCycleStatus = :lifeCycleStatus")
    List<Career> findByCarrerTyp(CareerType c,  LifeCycleStatus lifeCycleStatus);

    @Query(value = "select * from carrers limit :lim offset :of ", nativeQuery = true)
    List<Career> findCareerWithLimit(long lim, long of);

}
