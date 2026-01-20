package mz.org.fgh.mentoring.repository.user;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    @Query(value = "select u from User u inner join u.employee e inner join e.partner p WHERE u.id =:userId")
    User fetchByUserId(Long userId);

    Optional<User> findByUuid(String uuid);

    @Query(
            value = "SELECT DISTINCT u FROM User u " +
                    "INNER JOIN u.employee e " +
                    "WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "OR LOWER(e.nuit) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "ORDER BY e.name",
            countQuery = "SELECT COUNT(DISTINCT u) FROM User u " +
                    "INNER JOIN u.employee e " +
                    "WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "OR LOWER(e.nuit) LIKE LOWER(CONCAT('%', :query, '%')) " +
                    "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))"
    )
    Page<User> searchByFilters(@Nullable String query, Pageable pageable);


    List<User> findByUuidIn(List<String> uuids);

    Optional<User> findByEmployee(Employee employee);
}

