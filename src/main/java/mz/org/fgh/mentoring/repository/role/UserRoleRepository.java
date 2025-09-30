package mz.org.fgh.mentoring.repository.role;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.List;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    @Override
    List<UserRole> findAll();

    @Query("select ur from UserRole ur where ur.user.id = :userId")
    UserRole findByUserId(Long userId);
    
    @Query("select ur from UserRole ur where ur.user.id = :userId and ur.role.id= :roleId")
    UserRole findByUserIdAndRoleId(Long userId,Long roleId);

    void deleteByUser(User user);
}
