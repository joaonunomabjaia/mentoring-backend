package mz.org.fgh.mentoring.repository.employee;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.partner.Partner;
import mz.org.fgh.mentoring.entity.professionalcategory.ProfessionalCategory;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.EmailDuplicationException;
import mz.org.fgh.mentoring.error.NuitDuplicationException;
import mz.org.fgh.mentoring.error.PhoneDuplicationException;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    //Employee findByUuid(String uuid);
    Optional<Employee> findByUuid(String uuid);


    Optional<Employee> findByEmail(String uuid);

    Employee createOrUpdate(Employee employee, User user) throws NuitDuplicationException, EmailDuplicationException, PhoneDuplicationException;

    @Query(value = "select e from Employee e join e.professionalCategory pc where pc.id = :professionalCategoryId ")
    List<Employee> findByProfessionalCategory(Long professionalCategoryId);

    long countByProfessionalCategory(ProfessionalCategory category);

    long countByPartner(Partner partner);
}
