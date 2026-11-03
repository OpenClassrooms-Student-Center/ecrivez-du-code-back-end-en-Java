package com.hamac.leave;

import static org.assertj.core.api.Assertions.assertThat;

import com.hamac.employee.Employee;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class InternshipPolicyTests {

    private final InternshipPolicy policy = new InternshipPolicy();

    @Test
    void grantsTwoDaysPerFullMonth() {
        var employee = new Employee();
        employee.setHireDate(LocalDate.of(2026, 3, 15));

        var entitlement = policy.computeEntitlement(employee, LocalDate.of(2026, 10, 20));

        assertThat(entitlement).isEqualTo(14.0);
    }

    @Test
    void grantsNothingBeforeInternshipStarts() {
        var employee = new Employee();
        employee.setHireDate(LocalDate.of(2026, 11, 2));

        var entitlement = policy.computeEntitlement(employee, LocalDate.of(2026, 10, 26));

        assertThat(entitlement).isEqualTo(0.0);
    }
}
