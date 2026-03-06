package org.pm09.ps12.lms.leaveapplication.service;

import org.pm09.ps12.lms.leaveapplication.dto.EmployeeDto;
import org.pm09.ps12.lms.leaveapplication.entity.Employee;
import org.pm09.ps12.lms.leaveapplication.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EmployeeDto registerEmployee(EmployeeDto employeeDto) {
        // Check if username already exists
        if (employeeRepository.findByUsername(employeeDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already in use");
        }

        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setUsername(employeeDto.getUsername()); // <-- use username
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        employee.setRole(Employee.Role.valueOf(employeeDto.getRole()));

        Employee saved = employeeRepository.save(employee);

        EmployeeDto response = new EmployeeDto();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setUsername(saved.getUsername()); // <-- include username
        response.setRole(saved.getRole().name());
        return response;
    }
}