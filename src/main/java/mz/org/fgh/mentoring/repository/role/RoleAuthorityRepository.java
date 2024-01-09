package mz.org.fgh.mentoring.repository.role;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.role.RoleAuthority;

@Repository
public interface RoleAuthorityRepository extends CrudRepository<RoleAuthority, Long> {
}
