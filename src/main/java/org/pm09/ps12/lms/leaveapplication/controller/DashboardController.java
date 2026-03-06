package org.pm09.ps12.lms.leaveapplication.controller;

import org.pm09.ps12.lms.leaveapplication.dto.LeaveRequestDto;
import org.pm09.ps12.lms.leaveapplication.entity.Employee;
import org.pm09.ps12.lms.leaveapplication.entity.LeaveType;
import org.pm09.ps12.lms.leaveapplication.service.LeaveService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final LeaveService leaveService;

    public DashboardController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Employee employee, Model model) {
        // Get employee's leave requests
        List<LeaveRequestDto> requests = leaveService.getLeavesByEmployee(employee.getId());
        model.addAttribute("leaveRequests", requests);
        model.addAttribute("employee", employee);

        // Calculate leave balances
        Map<String, Map<String, Object>> balances = new HashMap<>();
        int currentYear = LocalDate.now().getYear();

        for (LeaveType type : LeaveType.values()) {
            int used = leaveService.getUsedLeaveDays(employee.getId(), type, currentYear);
            int total = leaveService.getEntitlement(employee.getRole(), type);
            int remaining = total - used;
            int percentage = total == 0 ? 0 : (used * 100 / total);

            Map<String, Object> typeBalance = new HashMap<>();
            typeBalance.put("used", used);
            typeBalance.put("total", total);
            typeBalance.put("remaining", remaining);
            typeBalance.put("percentage", percentage);
            balances.put(type.name(), typeBalance);
        }
        model.addAttribute("balances", balances);

        // If manager, load pending approvals
        if (employee.getRole() == Employee.Role.MANAGER) {
            List<LeaveRequestDto> pendingLeaves = leaveService.getAllPendingLeaves();
            model.addAttribute("pendingLeaves", pendingLeaves);
        }

        return "dashboard";
    }

    @PostMapping("/apply-leave")
    public String applyLeave(@AuthenticationPrincipal Employee employee,
                             @RequestParam LeaveType leaveType,
                             @RequestParam LocalDate startDate,
                             @RequestParam LocalDate endDate,
                             @RequestParam String reason) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setEmployeeId(employee.getId());
        dto.setLeaveType(leaveType);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setReason(reason);

        leaveService.applyForLeave(dto);
        return "redirect:/dashboard";
    }

    @PostMapping("/approve-leave")
    public String approveLeave(@RequestParam Long leaveId) {
        leaveService.approveLeave(leaveId);
        return "redirect:/dashboard";
    }

    @PostMapping("/reject-leave")
    public String rejectLeave(@RequestParam Long leaveId) {
        leaveService.rejectLeave(leaveId);
        return "redirect:/dashboard";
    }
}