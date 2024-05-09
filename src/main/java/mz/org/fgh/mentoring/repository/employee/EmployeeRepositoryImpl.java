package mz.org.fgh.mentoring.repository.employee;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.EmailDuplicationException;
import mz.org.fgh.mentoring.error.NuitDuplicationException;
import mz.org.fgh.mentoring.error.PhoneDuplicationException;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.PersistenceException;
import java.util.Optional;
import java.util.UUID;

@Repository
public abstract class EmployeeRepositoryImpl implements EmployeeRepository{

    public Employee createOrUpdate(Employee employee, User user) throws NuitDuplicationException, EmailDuplicationException, PhoneDuplicationException {
        try {
            if(!Utilities.stringHasValue(employee.getUuid())) {
                return createEmployee(employee, user);
            }
            Optional<Employee> possibleEmployee = findByUuid(employee.getUuid());

            if (possibleEmployee.isPresent()) {
                employee.setCreatedBy(possibleEmployee.get().getCreatedBy());
                employee.setCreatedAt(possibleEmployee.get().getCreatedAt());
                employee.setLifeCycleStatus(possibleEmployee.get().getLifeCycleStatus());

                employee.setId(possibleEmployee.get().getId());
                employee.setUpdatedBy(user.getUuid());
                employee.setUpdatedAt(DateUtils.getCurrentDate());
                return update(employee);
            } else {
                return createEmployee(employee, user);
            }
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();

                // Extract information about the constraint violation
                String constraintName = constraintViolationException.getConstraintName();
                String violatingValue = constraintViolationException.getConstraintName();

                if (constraintName.equalsIgnoreCase("NUIT")) {
                    throw new NuitDuplicationException("Ja existe no sistema uma pessoa registada com o NUIT: "+employee.getNuit());
                }

                if (constraintName.equalsIgnoreCase("EMAIL")) {
                    throw new EmailDuplicationException("Ja existe no sistema uma pessoa registada com o EMAIL: "+employee.getEmail());
                }

                if (constraintName.equalsIgnoreCase("PHONE_NUMBER")) {
                    throw new PhoneDuplicationException("Ja existe no sistema uma pessoa registada com o numero de telefone: "+employee.getPhoneNumber());
                }
                // Build a user-friendly message based on the constraint information
                String message = "Database constraint violation occurred:\n";
                message += "Constraint name: " + constraintName + "\n";
                message += "Violating value: " + violatingValue + "\n";
                message += "Please ensure that your data complies with the database constraints.";

                // Display the user-friendly message
                System.err.println(message);
            } else {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Employee createEmployee(Employee employee, User user) {
        employee.setCreatedBy(user.getUuid());
        employee.setUuid(UUID.randomUUID().toString());
        employee.setCreatedAt(DateUtils.getCurrentDate());
        employee.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
        return save(employee);
    }
}
