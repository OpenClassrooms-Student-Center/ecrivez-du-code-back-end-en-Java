package com.hamac.service;

import java.util.Arrays;

public enum ContractType {
    PERMANENT("CDI"),
    FIXED_TERM("CDD"),
    APPRENTICESHIP("Alternance");

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
