package mz.org.fgh.mentoring.repository.employee;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.employee.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
