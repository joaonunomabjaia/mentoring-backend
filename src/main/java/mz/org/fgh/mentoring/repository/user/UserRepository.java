package mz.org.fgh.mentoring.repository.user;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    @Query(value = "select u from User u inner join u.employee e inner join e.partner p WHERE u.id =:userId")
    User fetchByUserId(Long userId);
    @Override
    List<User> findAll();
}
