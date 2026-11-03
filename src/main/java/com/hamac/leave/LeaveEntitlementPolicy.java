package com.hamac.leave;

import com.hamac.employee.ContractType;
import com.hamac.employee.Employee;
import java.time.LocalDate;

interface LeaveEntitlementPolicy {

    boolean supports(ContractType type);

    double computeEntitlement(Employee employee, LocalDate today);
}
