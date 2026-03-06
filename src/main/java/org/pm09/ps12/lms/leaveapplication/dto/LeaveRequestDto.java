package org.pm09.ps12.lms.leaveapplication.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.pm09.ps12.lms.leaveapplication.entity.LeaveType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class LeaveRequestDto {
    private Long id;
    private Long employeeId;
    private String employeeName;

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;

    private String reason;
    private String status;

    // Helper method for formatted date in JSP
    public String getFormattedStartDate() {
        return startDate != null ? startDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) : "";
    }

    public String getFormattedEndDate() {
        return endDate != null ? endDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) : "";
    }
}