package com.hamac.leave;

import com.hamac.employee.Employee;

public interface LeaveNotifier {

    void notify(Employee employee, String message);
}
