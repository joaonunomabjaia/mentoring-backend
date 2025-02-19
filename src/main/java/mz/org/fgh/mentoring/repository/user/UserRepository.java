package mz.org.fgh.mentoring.repository.user;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;

import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    @Query(value = "select u from User u inner join u.employee e inner join e.partner p WHERE u.id =:userId")
    User fetchByUserId(Long userId);

    Optional<User> findByUuid(String uuid);

    @Query(value = "SELECT DISTINCT u FROM User u " +
            "INNER JOIN u.employee e " +
            "WHERE (:name IS NULL OR e.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:nuit IS NULL OR e.nuit LIKE CONCAT('%', :nuit, '%')) " +
            "AND (:username IS NULL OR u.username LIKE CONCAT('%', :username, '%')) ",
            countQuery = "SELECT COUNT(DISTINCT u) FROM User u " +
                    "INNER JOIN u.employee e " +
                    "WHERE (:name IS NULL OR e.name LIKE CONCAT('%', :name, '%')) " +
                    "AND (:nuit IS NULL OR e.nuit LIKE CONCAT('%', :nuit, '%')) " +
                    "AND (:username IS NULL OR u.username LIKE CONCAT('%', :username, '%')) "
    )
    Page<User> search(@Nullable String name,@Nullable String nuit,@Nullable String username, Pageable pageable);

    List<User> findByUuidIn(List<String> uuids);
}

