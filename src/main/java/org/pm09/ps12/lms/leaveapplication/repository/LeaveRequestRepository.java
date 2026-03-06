package org.pm09.ps12.lms.leaveapplication.repository;

import org.pm09.ps12.lms.leaveapplication.entity.Employee;
import org.pm09.ps12.lms.leaveapplication.entity.LeaveRequest;
import org.pm09.ps12.lms.leaveapplication.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee(Employee employee);
    List<LeaveRequest> findByStatus(LeaveRequest.Status status);

    List<LeaveRequest> findByEmployeeIdAndLeaveTypeAndStatusAndStartDateBetween(
            Long employeeId,
            LeaveType leaveType,
            LeaveRequest.Status status,
            LocalDate startDate,
            LocalDate endDate
    );

    List<LeaveRequest> findByStatusOrderByStartDateAsc(LeaveRequest.Status status);
}