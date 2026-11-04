package com.hamac.controller;

import com.hamac.model.EmployeeRepository;
import com.hamac.model.LeaveRequestRepository;
import com.hamac.service.NotificationService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveController(EmployeeRepository employeeRepository, LeaveRequestRepository leaveRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @GetMapping("/approve")
    public String approve(@RequestParam long id) {
        var req = leaveRequestRepository.findAll().stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
        var e = employeeRepository.findById(req.getEmployeeId()).orElse(null);

        double d;
        if (e.getContractType().equals("CDI")) {
            d = 25;
        } else if (e.getContractType().equals("CDD")) {
            long m = ChronoUnit.DAYS.between(e.getHireDate(), LocalDate.now()) / 30;
            d = m * 2.08;
        } else if (e.getContractType().equals("Alternance")) {
            d = 30;
        } else {
            d = 0;
        }

        if (d - e.getDaysTaken() < req.getWorkingDays()) {
            req.setStatus("REFUSED");
            leaveRequestRepository.save(req);
            return "Refused: insufficient balance";
        }

        req.setStatus("APPROVED");
        e.setDaysTaken(e.getDaysTaken() + req.getWorkingDays());
        leaveRequestRepository.save(req);
        employeeRepository.save(e);

        new NotificationService().notify(e, "Votre demande de congés est approuvée.");
        return "Approved";
    }

    @GetMapping("/balance")
    public Object getBalance(@RequestParam long employeeId) {
        var e = employeeRepository.findById(employeeId).orElse(null);
        if (e == null) {
            return "Employee not found";
        }

        double days;
        if (e.getContractType().equals("CDI")) {
            days = 25;
        } else if (e.getContractType().equals("CDD")) {
            double months = Math.ceil(ChronoUnit.DAYS.between(e.getHireDate(), LocalDate.now()) / 30.0);
            days = months * 2.08;
        } else if (e.getContractType().equals("Alternance")) {
            days = 25 + 5;
        } else {
            days = 0;
        }

        return days - e.getDaysTaken();
    }
}
