package com.hamac.leave;

import com.hamac.employee.ContractType;
import com.hamac.employee.Employee;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
final class InternshipPolicy implements LeaveEntitlementPolicy {

    private static final long DAYS_PER_MONTH = 30;
    private static final double DAYS_PER_FULL_MONTH = 2;

    @Override
    public boolean supports(ContractType type) {
        return type == ContractType.INTERNSHIP;
    }

    @Override
    public double computeEntitlement(Employee employee, LocalDate today) {
        var daysSinceHire = ChronoUnit.DAYS.between(employee.getHireDate(), today);
        var fullMonths = Math.floorDiv(daysSinceHire, DAYS_PER_MONTH);
        return Math.max(0, fullMonths) * DAYS_PER_FULL_MONTH;
    }
}
