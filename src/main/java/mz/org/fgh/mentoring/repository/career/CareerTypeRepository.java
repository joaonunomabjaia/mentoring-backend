package mz.org.fgh.mentoring.repository.career;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.career.CareerType;

import java.util.List;

@Repository
public interface CareerTypeRepository extends CrudRepository<CareerType, Long> {
    @Query(value = "select * from career_type limit :lim offset :of ", nativeQuery = true)
    List<CareerType> findCareerTypeWithLimit(long lim, long of);
    List<CareerType> findAll();
}
