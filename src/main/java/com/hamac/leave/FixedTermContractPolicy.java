package com.hamac.leave;

import com.hamac.employee.ContractType;
import com.hamac.employee.Employee;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
final class FixedTermContractPolicy implements LeaveEntitlementPolicy {

    private static final long DAYS_PER_MONTH = 30;

    private final double daysPerFullMonth;

    FixedTermContractPolicy(LeaveProperties properties) {
        this.daysPerFullMonth = properties.fixedTermDaysPerFullMonth();
    }

    @Override
    public boolean supports(ContractType type) {
        return type == ContractType.FIXED_TERM;
    }

    @Override
    public double computeEntitlement(Employee employee, LocalDate today) {
        var daysSinceHire = ChronoUnit.DAYS.between(employee.getHireDate(), today);
        var fullMonths = Math.floorDiv(daysSinceHire, DAYS_PER_MONTH);
        return fullMonths * daysPerFullMonth;
    }
}
