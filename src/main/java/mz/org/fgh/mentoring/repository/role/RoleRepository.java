package mz.org.fgh.mentoring.repository.role;

import java.util.List;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.role.Role;

@Repository
public interface RoleRepository  extends CrudRepository<Role, Long> {
    @Override
    List<Role> findAll();
}
