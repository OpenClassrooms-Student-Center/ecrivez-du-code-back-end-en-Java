package com.hamac.controller;

import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class PageController {

    @GetMapping("/")
    String leaveRequests(Model model) {
        model.addAttribute("today", LocalDate.now());
        return "leave-requests";
    }
}
