package mz.org.fgh.mentoring.repository.tutor;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.career.CareerType;

import java.util.List;

@Repository
public interface CareerTypeRepository extends CrudRepository<CareerType, Long> {

    List<CareerType> findAll();
}
