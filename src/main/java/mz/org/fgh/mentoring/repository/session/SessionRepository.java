package mz.org.fgh.mentoring.repository.session;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.session.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
}
