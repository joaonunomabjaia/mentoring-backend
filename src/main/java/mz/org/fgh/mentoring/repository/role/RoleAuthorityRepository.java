package mz.org.fgh.mentoring.repository.role;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.role.Role;
import mz.org.fgh.mentoring.entity.role.RoleAuthority;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleAuthorityRepository extends CrudRepository<RoleAuthority, Long> {

    Set<RoleAuthority> findByRole(Role role);
}
