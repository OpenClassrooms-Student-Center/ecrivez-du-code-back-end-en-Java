package com.hamac.service;

import java.util.List;

public final class LeavePolicyResolver {

    private final List<LeaveEntitlementPolicy> policies;

    public LeavePolicyResolver(List<LeaveEntitlementPolicy> policies) {
        this.policies = List.copyOf(policies);
    }

    public LeaveEntitlementPolicy policyFor(ContractType type) {
        return policies.stream()
                .filter(policy -> policy.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No leave policy for " + type));
    }
}
