package com.hamac.leave;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/employees")
class BalanceController {

    private final LeaveService leaveService;

    BalanceController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/{id}/balance")
    BalanceDto getBalance(@PathVariable long id) {
        return leaveService.getBalance(id)
                .map(balance -> new BalanceDto(id, balance))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ce salarié n’existe pas."));
    }
}
