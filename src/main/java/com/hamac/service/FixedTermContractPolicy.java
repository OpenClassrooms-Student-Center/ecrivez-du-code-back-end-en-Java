package com.hamac.service;

import com.hamac.model.Employee;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class FixedTermContractPolicy implements LeaveEntitlementPolicy {

    private static final double DAYS_PER_FULL_MONTH = 2.08;
    private static final long DAYS_PER_MONTH = 30;

    @Override
    public boolean supports(ContractType type) {
        return type == ContractType.FIXED_TERM;
    }

    @Override
    public double computeEntitlement(Employee employee, LocalDate today) {
        var daysSinceHire = ChronoUnit.DAYS.between(employee.getHireDate(), today);
        var fullMonths = Math.floorDiv(daysSinceHire, DAYS_PER_MONTH);
        return fullMonths * DAYS_PER_FULL_MONTH;
    }
}
