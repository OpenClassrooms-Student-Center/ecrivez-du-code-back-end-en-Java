package com.hamac.employee;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface EmployeeRepository extends Repository<Employee, Long> {

    Optional<Employee> findById(Long id);

    Employee save(Employee employee);
}
