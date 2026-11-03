package com.hamac.leave;

import com.hamac.employee.ContractType;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
final class LeavePolicyResolver {

    private final List<LeaveEntitlementPolicy> policies;

    LeavePolicyResolver(List<LeaveEntitlementPolicy> policies) {
        for (var type : ContractType.values()) {
            var count = policies.stream().filter(policy -> policy.supports(type)).count();
            if (count != 1) {
                throw new IllegalStateException(count + " leave policies found for " + type + ", expected 1");
            }
        }
        this.policies = List.copyOf(policies);
    }

    LeaveEntitlementPolicy policyFor(ContractType type) {
        return policies.stream()
                .filter(policy -> policy.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No leave policy for " + type));
    }
}
