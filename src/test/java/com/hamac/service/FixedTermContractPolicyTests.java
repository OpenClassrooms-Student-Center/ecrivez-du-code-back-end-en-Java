package com.hamac.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.hamac.model.Employee;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class FixedTermContractPolicyTests {

    @Test
    void countsOnlyFullMonths() {
        var policy = new FixedTermContractPolicy();
        var employee = new Employee();
        employee.setHireDate(LocalDate.of(2026, 3, 15));

        var entitlement = policy.computeEntitlement(employee, LocalDate.of(2026, 10, 20));

        assertThat(entitlement).isCloseTo(14.56, within(0.01));
    }
}
