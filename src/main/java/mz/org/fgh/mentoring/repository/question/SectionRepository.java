package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.question.Section;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {
    List<Section> findAll();

    Optional<Section> findByUuid(String uuid);
}
