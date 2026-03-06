package org.pm09.ps12.lms.leaveapplication.repository;

import org.pm09.ps12.lms.leaveapplication.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);  // <-- find by username for login

    boolean existsByUsername(String username);           // <-- check username uniqueness

    Optional<Employee> findByEmail(String email);       // <-- optional, for contact purposes

    boolean existsByEmail(String email);                // <-- optional, for registration validation
}