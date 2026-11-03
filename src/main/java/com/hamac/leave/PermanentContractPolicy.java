package com.hamac.leave;

import com.hamac.employee.ContractType;
import com.hamac.employee.Employee;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
final class PermanentContractPolicy implements LeaveEntitlementPolicy {

    private static final double ANNUAL_DAYS = 25;

    @Override
    public boolean supports(ContractType type) {
        return type == ContractType.PERMANENT;
    }

    @Override
    public double computeEntitlement(Employee employee, LocalDate today) {
        return ANNUAL_DAYS;
    }
}
