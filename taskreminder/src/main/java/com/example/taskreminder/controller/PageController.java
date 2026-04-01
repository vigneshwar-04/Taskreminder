package com.example.taskreminder.controller;

import com.example.taskreminder.model.Taskmodel;
import com.example.taskreminder.service.TaskManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    private final TaskManager service;

    public PageController(TaskManager service) {
        this.service = service;
    }

    // ✅ FIX: redirect root
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // ✅ ONLY ONE login mapping
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("tasks", service.getAllTasks());
        return "dashboard";
    }
}