package com.hamac.service;

import com.hamac.model.Employee;
import java.time.LocalDate;

public final class ApprenticeshipPolicy implements LeaveEntitlementPolicy {

    private static final double ANNUAL_DAYS = 25;
    private static final double EXAM_PREPARATION_DAYS = 5;

    @Override
    public boolean supports(ContractType type) {
        return type == ContractType.APPRENTICESHIP;
    }

    @Override
    public double computeEntitlement(Employee employee, LocalDate today) {
        return ANNUAL_DAYS + EXAM_PREPARATION_DAYS;
    }
}
