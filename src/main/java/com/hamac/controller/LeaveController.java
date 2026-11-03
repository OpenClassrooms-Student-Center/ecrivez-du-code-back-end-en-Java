package com.hamac.controller;

import com.hamac.model.EmployeeRepository;
import com.hamac.model.LeaveRequestRepository;
import com.hamac.service.ApprenticeshipPolicy;
import com.hamac.service.FixedTermContractPolicy;
import com.hamac.service.LeavePolicyResolver;
import com.hamac.service.LeaveService;
import com.hamac.service.NotificationService;
import com.hamac.service.PermanentContractPolicy;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(EmployeeRepository employeeRepository,
                           LeaveRequestRepository leaveRequestRepository) {
        // Assemblage provisoire : il sera confié au conteneur Spring au chapitre 4.
        var policies = new LeavePolicyResolver(List.of(
                new PermanentContractPolicy(),
                new FixedTermContractPolicy(),
                new ApprenticeshipPolicy()));
        this.leaveService = new LeaveService(employeeRepository, leaveRequestRepository,
                policies, new NotificationService());
    }

    @GetMapping("/balance")
    public Object getBalance(@RequestParam long employeeId) {
        // Contrat de l’API inchangé jusqu’au chapitre 5.
        var balance = leaveService.getBalance(employeeId);
        return balance.isPresent() ? balance.get() : "Employee not found";
    }

    @GetMapping("/approve")
    public String approve(@RequestParam long id) {
        return switch (leaveService.approve(id)) {
            case APPROVED -> "Approved";
            case INSUFFICIENT_BALANCE -> "Refused: insufficient balance";
            case NOT_FOUND -> "Leave request not found";
        };
    }
}
