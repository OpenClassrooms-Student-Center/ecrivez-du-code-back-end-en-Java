package com.hamac.service;

import com.hamac.model.Employee;

public interface LeaveNotifier {

    void notify(Employee employee, String message);
}
