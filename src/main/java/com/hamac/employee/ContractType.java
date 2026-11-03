package com.hamac.employee;

import java.util.Arrays;

public enum ContractType {
    PERMANENT("CDI"),
    FIXED_TERM("CDD"),
    APPRENTICESHIP("Alternance"),
    INTERNSHIP("Stage");

    private final String code;

    ContractType(String code) {
        this.code = code;
    }

    public static ContractType fromCode(String code) {
        return Arrays.stream(values())
                .filter(type -> type.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown contract type: " + code));
    }
}
