package com.hamac.leave;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

record CreateLeaveRequestDto(
        @NotNull @Positive Long employeeId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate) {

    @JsonIgnore
    @AssertTrue(message = "La date de fin doit être postérieure ou égale à la date de début.")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}
