package mz.org.fgh.mentoring.repository.employee;

import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.user.User;


public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Employee findByUuid(String uuid);

    Employee createOrUpdate(Employee employee, User user);
}
