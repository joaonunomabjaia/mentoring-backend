package mz.org.fgh.mentoring.repository.session;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.session.SessionStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionStatusRepository extends CrudRepository<SessionStatus, Long> {

    @Override
    List<SessionStatus> findAll();

    @Override
    Optional<SessionStatus> findById(@NotNull Long id);

    Optional<SessionStatus> findByUuid(String uuid);

    SessionStatus findByCode(String code);
}
