package mz.org.fgh.mentoring.repository.employee;

import io.micronaut.data.repository.CrudRepository;
import jakarta.annotation.Nullable;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.EmailDuplicationException;
import mz.org.fgh.mentoring.error.NuitDuplicationException;
import mz.org.fgh.mentoring.error.PhoneDuplicationException;

import java.util.Optional;


public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    //Employee findByUuid(String uuid);
    Optional<Employee> findByUuid(@Nullable String uuid);

    Employee createOrUpdate(Employee employee, User user) throws NuitDuplicationException, EmailDuplicationException, PhoneDuplicationException;
}
