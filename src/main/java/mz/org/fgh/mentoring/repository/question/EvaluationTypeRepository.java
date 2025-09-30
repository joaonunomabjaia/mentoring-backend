package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationType;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationTypeRepository extends CrudRepository<EvaluationType, Long> {

    List<EvaluationType> findAll();
    EvaluationType getByCode(String code);
    Optional<EvaluationType> findByUuid(String uuid);
}
