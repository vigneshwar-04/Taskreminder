package com.example.taskreminder.controller;

import com.example.taskreminder.entity.Task;
import com.example.taskreminder.repository.TaskRepository;
import com.example.taskreminder.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    private boolean isNotLoggedIn(HttpSession session) {
        return (session == null || session.getAttribute("userEmail") == null);
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "all") String filter,
                            Model model,
                            HttpSession session) {

        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }

        String email = (String) session.getAttribute("userEmail");

        List<Task> tasks;

        switch (filter.toLowerCase()) {
            case "pending":
                tasks = taskRepository.getTasksByStatus(email, "PENDING");
                break;
            case "completed":
                tasks = taskRepository.getTasksByStatus(email, "COMPLETED");
                break;
            case "overdue":
                tasks = taskRepository.getTasksByStatus(email, "OVERDUE");
                break;
            default:
                tasks = taskRepository.getTasksByEmail(email);
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("total", taskRepository.countAll(email));
        model.addAttribute("completed", taskRepository.countByStatus(email, "COMPLETED"));
        model.addAttribute("pending", taskRepository.countByStatus(email, "PENDING"));
        model.addAttribute("overdue", taskRepository.countByStatus(email, "OVERDUE"));

        return "dashboard";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task,
                          HttpSession session) {

        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }

        String email = (String) session.getAttribute("userEmail");

        if (task.getStatus() == null || task.getStatus().isEmpty()) {
            task.setStatus("PENDING");
        }

        taskRepository.addTask(task, email);

        emailService.sendEmail(
                email,
                "Task Added",
                "Your task '" + task.getTitle() + "' added successfully!"
        );

        return "redirect:/tasks/dashboard";
    }

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

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id,
                             HttpSession session) {

        if (isNotLoggedIn(session)) {
            return "redirect:/login";
        }

        taskRepository.deleteTask(id);

        return "redirect:/tasks/dashboard";
    }

    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response,
                          HttpSession session) throws Exception {

        if (session.getAttribute("userEmail") == null) {
            response.sendRedirect("/login");
            return;
        }

        String email = (String) session.getAttribute("userEmail");

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=tasks.csv");

        PrintWriter writer = response.getWriter();

        writer.println("Title,Description,Time,Status");

        for (Task task : taskRepository.getTasksByEmail(email)) {
            writer.println(task.getTitle() + "," +
                    task.getDescription() + "," +
                    task.getTime() + "," +
                    task.getStatus());
        }

        writer.flush();
    }
}