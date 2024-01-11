package mz.org.fgh.mentoring.repository.authority;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.authority.Authority;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}
