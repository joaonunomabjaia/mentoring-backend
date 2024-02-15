package mz.org.fgh.mentoring.service.employee;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.dto.employee.EmployeeDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.repository.employee.EmployeeRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;

@Singleton
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    public EmployeeDTO getById(Long id){

        Employee employee = this.employeeRepository.findById(id).get();

        return new EmployeeDTO(employee);
    }

    public EmployeeDTO updade(EmployeeDTO employeeDTO){

        Employee employee = this.employeeRepository.update( new Employee(employeeDTO));

        return new EmployeeDTO(employee);
    }
}
