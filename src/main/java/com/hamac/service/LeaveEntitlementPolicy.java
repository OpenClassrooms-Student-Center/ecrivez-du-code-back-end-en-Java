package com.hamac.service;

import com.hamac.model.Employee;
import java.time.LocalDate;

public interface LeaveEntitlementPolicy {

    boolean supports(ContractType type);

    double computeEntitlement(Employee employee, LocalDate today);
}
