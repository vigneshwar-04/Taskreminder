package com.example.taskreminder.controller;

import com.example.taskreminder.model.Taskmodel;
import com.example.taskreminder.service.TaskManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    private final TaskManager service;

    public PageController(TaskManager service) {
        this.service = service;
    }

    // Login Page
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    // Login Action
    @PostMapping("/login")
    public String login() {
        return "redirect:/dashboard";
    }

    // Dashboard Page
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("tasks", service.getAllTasks());
        return "dashboard";
    }

    // Add Task
    @PostMapping("/addTask")
    public String addTask(Taskmodel task, RedirectAttributes redirectAttributes) {
        service.addTask(task);

        redirectAttributes.addFlashAttribute("success", "Task Added Successfully!");
        redirectAttributes.addFlashAttribute("type", "add"); // GREEN

        return "redirect:/dashboard";
    }

    // Delete Task
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.deleteTask(id);

        redirectAttributes.addFlashAttribute("success", "Task Deleted Successfully!");
        redirectAttributes.addFlashAttribute("type", "delete"); // RED

        return "redirect:/dashboard";
    }
}