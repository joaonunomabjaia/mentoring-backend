package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.question.ResponseType;

import java.util.List;

@Repository
public interface ResponseTypeRepository extends CrudRepository<ResponseType, Long> {

    List<ResponseType> findAll();
    ResponseType getByCode(String code);
}
