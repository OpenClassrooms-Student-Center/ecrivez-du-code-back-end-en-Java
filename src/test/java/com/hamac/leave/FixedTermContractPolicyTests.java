package com.hamac.leave;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.hamac.employee.Employee;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class FixedTermContractPolicyTests {

    @Test
    void countsOnlyFullMonths() {
        var policy = new FixedTermContractPolicy(new LeaveProperties(2.08));
        var employee = new Employee();
        employee.setHireDate(LocalDate.of(2026, 3, 15));

        var entitlement = policy.computeEntitlement(employee, LocalDate.of(2026, 10, 20));

        assertThat(entitlement).isCloseTo(14.56, within(0.01));
    }
}
