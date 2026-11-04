package com.hamac.leave;

import com.hamac.employee.ContractType;
import com.hamac.employee.Employee;
import com.hamac.employee.EmployeeRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeavePolicyResolver policies;
    private final LeaveNotifier notifier;

    LeaveService(EmployeeRepository employeeRepository,
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

    public List<LeaveRequest> getRequests(LeaveStatus status) {
        var byStartDate = Sort.by("startDate");
        return status == null
                ? leaveRequestRepository.findAll(byStartDate)
                : leaveRequestRepository.findByStatus(status, byStartDate);
    }

    public Optional<LeaveRequest> getRequest(long requestId) {
        return leaveRequestRepository.findById(requestId);
    }

    @Transactional
    public Optional<LeaveRequest> create(long employeeId, LocalDate startDate, LocalDate endDate) {
        if (employeeRepository.findById(employeeId).isEmpty()) {
            return Optional.empty();
        }
        var request = new LeaveRequest();
        request.setEmployeeId(employeeId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setWorkingDays(countWorkingDays(startDate, endDate));
        request.setStatus(LeaveStatus.PENDING);
        return Optional.of(leaveRequestRepository.save(request));
    }

    @Transactional
    public ApprovalResult approve(long requestId) {
        var found = leaveRequestRepository.findById(requestId);
        if (found.isEmpty()) {
            return ApprovalResult.NOT_FOUND;
        }
        var request = found.get();
        if (request.getStatus() != LeaveStatus.PENDING) {
            return ApprovalResult.NOT_PENDING;
        }
        var employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow();

        if (computeBalance(employee) < request.getWorkingDays()) {
            request.setStatus(LeaveStatus.REFUSED);
            leaveRequestRepository.save(request);
            return ApprovalResult.INSUFFICIENT_BALANCE;
        }

        request.setStatus(LeaveStatus.APPROVED);
        employee.setDaysTaken(employee.getDaysTaken() + request.getWorkingDays());
        leaveRequestRepository.save(request);
        employeeRepository.save(employee);

        notifier.notify(employee, "Votre demande de congés est approuvée.");
        return ApprovalResult.APPROVED;
    }

    @Transactional
    public RefusalResult refuse(long requestId) {
        var found = leaveRequestRepository.findById(requestId);
        if (found.isEmpty()) {
            return RefusalResult.NOT_FOUND;
        }
        var request = found.get();
        if (request.getStatus() != LeaveStatus.PENDING) {
            return RefusalResult.NOT_PENDING;
        }
        request.setStatus(LeaveStatus.REFUSED);
        leaveRequestRepository.save(request);
        return RefusalResult.REFUSED;
    }

    private double computeBalance(Employee employee) {
        var type = ContractType.fromCode(employee.getContractType());
        return policies.policyFor(type).computeEntitlement(employee, LocalDate.now())
                - employee.getDaysTaken();
    }

    private static int countWorkingDays(LocalDate startDate, LocalDate endDate) {
        return (int) startDate.datesUntil(endDate.plusDays(1))
                .filter(day -> day.getDayOfWeek() != DayOfWeek.SATURDAY
                        && day.getDayOfWeek() != DayOfWeek.SUNDAY)
                .count();
    }
}
