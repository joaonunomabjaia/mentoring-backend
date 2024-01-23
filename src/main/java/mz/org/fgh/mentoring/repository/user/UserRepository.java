package mz.org.fgh.mentoring.repository.user;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String userName);

}
