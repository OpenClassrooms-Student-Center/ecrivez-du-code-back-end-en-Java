package com.hamac.service;

import com.hamac.model.Employee;
import com.hamac.model.EmployeeRepository;
import com.hamac.model.LeaveRequestRepository;
import java.time.LocalDate;
import java.util.Optional;

public final class LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeavePolicyResolver policies;
    private final LeaveNotifier notifier;

    public LeaveService(EmployeeRepository employeeRepository,
                        LeaveRequestRepository leaveRequestRepository,
                        LeavePolicyResolver policies,
                        LeaveNotifier notifier) {
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.policies = policies;
        this.notifier = notifier;
    }

    public Optional<Double> getBalance(long employeeId) {
        return employeeRepository.findById(employeeId).map(this::computeBalance);
    }

    public ApprovalResult approve(long requestId) {
        // Filtrage en mémoire conservé : il sera corrigé au chapitre 4.
        var found = leaveRequestRepository.findAll().stream()
                .filter(r -> r.getId() == requestId)
                .findFirst();
        if (found.isEmpty()) {
            return ApprovalResult.NOT_FOUND;
        }
        var request = found.get();
        var employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow();

        if (computeBalance(employee) < request.getWorkingDays()) {
            request.setStatus("Refused");
            leaveRequestRepository.save(request);
            return ApprovalResult.INSUFFICIENT_BALANCE;
        }

        request.setStatus("Approved");
        employee.setDaysTaken(employee.getDaysTaken() + request.getWorkingDays());
        leaveRequestRepository.save(request);
        employeeRepository.save(employee);

        notifier.notify(employee, "Votre demande de congés est approuvée.");
        return ApprovalResult.APPROVED;
    }

    private double computeBalance(Employee employee) {
        var type = ContractType.fromCode(employee.getContractType());
        return policies.policyFor(type).computeEntitlement(employee, LocalDate.now())
                - employee.getDaysTaken();
    }
}
