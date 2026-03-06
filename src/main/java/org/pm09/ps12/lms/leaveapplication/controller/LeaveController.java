package org.pm09.ps12.lms.leaveapplication.controller;

import org.pm09.ps12.lms.leaveapplication.dto.ApiResponse;
import org.pm09.ps12.lms.leaveapplication.dto.LeaveRequestDto;
import org.pm09.ps12.lms.leaveapplication.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestDto>> applyLeave(@Valid @RequestBody LeaveRequestDto requestDto) {
        LeaveRequestDto result = leaveService.applyForLeave(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Leave applied successfully", result));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getLeavesByEmployee(@PathVariable Long employeeId) {
        List<LeaveRequestDto> leaves = leaveService.getLeavesByEmployee(employeeId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched successfully", leaves));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> getPendingLeaves() {
        List<LeaveRequestDto> leaves = leaveService.getAllPendingLeaves();
        return ResponseEntity.ok(new ApiResponse<>(true, "Fetched successfully", leaves));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> approveLeave(@PathVariable Long id) {
        LeaveRequestDto result = leaveService.approveLeave(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Leave approved", result));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> rejectLeave(@PathVariable Long id) {
        LeaveRequestDto result = leaveService.rejectLeave(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Leave rejected", result));
    }
}