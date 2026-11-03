package com.hamac.leave;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hamac.leave")
record LeaveProperties(double fixedTermDaysPerFullMonth) {

    public LeaveProperties {
        if (fixedTermDaysPerFullMonth <= 0) {
            throw new IllegalArgumentException("hamac.leave.fixed-term-days-per-full-month must be positive");
        }
    }
}
