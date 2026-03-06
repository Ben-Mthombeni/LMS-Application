package org.pm09.ps12.lms.leaveapplication.service;

import org.pm09.ps12.lms.leaveapplication.dto.LeaveRequestDto;
import org.pm09.ps12.lms.leaveapplication.entity.Employee;
import org.pm09.ps12.lms.leaveapplication.entity.LeaveRequest;
import org.pm09.ps12.lms.leaveapplication.entity.LeaveType;
import org.pm09.ps12.lms.leaveapplication.repository.EmployeeRepository;
import org.pm09.ps12.lms.leaveapplication.repository.LeaveRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    private LeaveRequestDto mapToDto(LeaveRequest leaveRequest) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(leaveRequest.getId());
        dto.setEmployeeId(leaveRequest.getEmployee().getId());
        dto.setEmployeeName(leaveRequest.getEmployee().getName());
        dto.setLeaveType(leaveRequest.getLeaveType());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus().name());
        return dto;
    }

    @Transactional
    public LeaveRequestDto applyForLeave(LeaveRequestDto requestDto) {
        Employee employee = employeeRepository.findById(requestDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (requestDto.getEndDate().isBefore(requestDto.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(requestDto.getLeaveType());
        leaveRequest.setStartDate(requestDto.getStartDate());
        leaveRequest.setEndDate(requestDto.getEndDate());
        leaveRequest.setReason(requestDto.getReason());
        leaveRequest.setStatus(LeaveRequest.Status.PENDING);

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        return mapToDto(saved);
    }

    public List<LeaveRequestDto> getLeavesByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return leaveRequestRepository.findByEmployee(employee)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestDto> getAllPendingLeaves() {
        return leaveRequestRepository.findByStatusOrderByStartDateAsc(LeaveRequest.Status.PENDING)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public int getUsedLeaveDays(Long employeeId, LeaveType type, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<LeaveRequest> requests = leaveRequestRepository
                .findByEmployeeIdAndLeaveTypeAndStatusAndStartDateBetween(
                        employeeId, type, LeaveRequest.Status.APPROVED, start, end);
        return requests.stream()
                .mapToInt(r -> (int) ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()) + 1)
                .sum();
    }

    @Transactional
    public LeaveRequestDto approveLeave(Long leaveId) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));
        leave.setStatus(LeaveRequest.Status.APPROVED);
        return mapToDto(leaveRequestRepository.save(leave));
    }

    @Transactional
    public LeaveRequestDto rejectLeave(Long leaveId) {
        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));
        leave.setStatus(LeaveRequest.Status.REJECTED);
        return mapToDto(leaveRequestRepository.save(leave));
    }

    public int getEntitlement(Employee.Role role, LeaveType type) {
        switch (type) {
            case ANNUAL: return role == Employee.Role.MANAGER ? 25 : 20;
            case SICK: return 10;
            case STUDY: return 5;
            default: return 0;
        }
    }
}
