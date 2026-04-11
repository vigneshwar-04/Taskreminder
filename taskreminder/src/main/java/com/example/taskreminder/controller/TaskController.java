package com.example.taskreminder.controller;

import com.example.taskreminder.entity.Task;
import com.example.taskreminder.repository.TaskRepository;
import com.example.taskreminder.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    // 🔐 COMMON SESSION CHECK METHOD (CLEAN WAY)
    private boolean isNotLoggedIn(HttpSession session) {
        return (session == null || session.getAttribute("userEmail") == null);
    }

    // ✅ DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "all") String filter,
                            Model model,
                            HttpSession session) {

        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }

        List<Task> tasks;

        switch (filter.toLowerCase()) {
            case "pending":
                tasks = taskRepository.getTasksByStatus("Pending");
                break;
            case "completed":
                tasks = taskRepository.getTasksByStatus("Completed");
                break;
            case "overdue":
                tasks = taskRepository.getTasksByStatus("Overdue");
                break;
            default:
                tasks = taskRepository.getAllTasks();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("total", taskRepository.countAll());
        model.addAttribute("completed", taskRepository.countByStatus("Completed"));
        model.addAttribute("pending", taskRepository.countByStatus("Pending"));
        model.addAttribute("overdue", taskRepository.countByStatus("Overdue"));

        return "dashboard";
    }

    // ✅ ADD TASK
    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task,
                          @RequestParam String email,
                          HttpSession session) {

        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }

        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            task.setStatus("Pending");
        }

        taskRepository.addTask(task);

        emailService.sendEmail(
                email,
                "Task Reminder",
                "Your task '" + task.getTitle() + "' added successfully!"
        );

        return "redirect:/tasks/dashboard";
    }

    // ✅ UPDATE STATUS
    @GetMapping("/updateStatus/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               HttpSession session) {

        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }

        taskRepository.updateStatus(id, status);

        return "redirect:/tasks/dashboard";
    }

    // ✅ DELETE TASK
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id,
                             HttpSession session) {

        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }

        taskRepository.deleteTask(id);

        return "redirect:/tasks/dashboard";
    }
}