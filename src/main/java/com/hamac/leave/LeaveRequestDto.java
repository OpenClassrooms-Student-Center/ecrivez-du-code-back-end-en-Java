package com.hamac.leave;

import java.time.LocalDate;

record LeaveRequestDto(
        long id,
        long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        int workingDays,
        LeaveStatus status) {

    static LeaveRequestDto from(LeaveRequest request) {
        return new LeaveRequestDto(
                request.getId(),
                request.getEmployeeId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getWorkingDays(),
                request.getStatus());
    }
}
