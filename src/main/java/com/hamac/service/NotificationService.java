package com.hamac.service;

import com.hamac.model.Employee;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private final List<String> failedNotifications = new ArrayList<>();

    public void notify(Employee employee, String message) {
        // Simulation : en production, ce service enverrait un e-mail.
        System.out.println("[notification] " + employee.getFirstName() + " " + employee.getLastName()
                + " : " + message);
    }

    public void sendWeeklySummary(List<Employee> managers) {
        for (Employee manager : managers) {
            System.out.println("[récapitulatif hebdomadaire] envoyé à " + manager.getFirstName()
                    + " " + manager.getLastName());
        }
    }

    public void retryFailedNotifications() {
        for (String notification : List.copyOf(failedNotifications)) {
            System.out.println("[nouvel essai] " + notification);
            failedNotifications.remove(notification);
        }
    }
}
