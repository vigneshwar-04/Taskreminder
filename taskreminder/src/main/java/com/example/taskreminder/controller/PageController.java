package com.example.taskreminder.controller;

import com.example.taskreminder.entity.Task;
import com.example.taskreminder.entity.User;
import com.example.taskreminder.repository.TaskRepository;
import com.example.taskreminder.repository.UserRepository;
import com.example.taskreminder.service.EmailService;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.util.Optional;

@Controller
public class PageController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final EmailService emailService;

    public PageController(UserRepository userRepository,
                          TaskRepository taskRepository,
                          EmailService emailService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           HttpSession session) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);
        session.setAttribute("userEmail", email);

        return "redirect:/dashboard";
    }

    @PostMapping("/doLogin")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(password)) {
                session.setAttribute("userEmail", email);
                return "redirect:/dashboard";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/sendOtp")
    public String sendOtp(@RequestParam String email,
                          HttpSession session,
                          org.springframework.ui.Model model) {

        String otp = String.valueOf((int)(Math.random() * 9000) + 1000);

        session.setAttribute("otp", otp);
        session.setAttribute("email", email);

        emailService.sendOTP(email, otp);

        model.addAttribute("showOtp", true);

        return "login";
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam String otp, HttpSession session) {

        String sessionOtp = (String) session.getAttribute("otp");

        if (otp.equals(sessionOtp)) {
            session.setAttribute("userEmail", session.getAttribute("email"));
            return "redirect:/dashboard";
        }

        return "redirect:/login";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam(required = false) Long id,
                          @RequestParam String title,
                          @RequestParam String description,
                          @RequestParam String time,
                          @RequestParam String status,
                          HttpSession session) {

        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        if (id != null) {
            taskRepository.updateTask(id, title, description,
                    java.time.LocalDateTime.parse(time + ":00"), status);
        } else {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTime(java.time.LocalDateTime.parse(time + ":00"));
            task.setStatus(status);

            taskRepository.addTask(task);

            emailService.sendEmail(
                    (String) session.getAttribute("userEmail"),
                    "Task Added",
                    "Your task '" + title + "' is added successfully."
            );
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/editTask")
    public String editTask(@RequestParam Long id,
                           HttpSession session,
                           org.springframework.ui.Model model) {

        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        Task task = taskRepository.getAllTasks()
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);

        model.addAttribute("editTask", task);
        model.addAttribute("tasks", taskRepository.getAllTasks());

        model.addAttribute("total", taskRepository.countAll());
        model.addAttribute("completed", taskRepository.countByStatus("COMPLETED"));
        model.addAttribute("pending", taskRepository.countByStatus("PENDING"));
        model.addAttribute("overdue", taskRepository.countByStatus("OVERDUE"));

        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, org.springframework.ui.Model model) {

        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        model.addAttribute("tasks", taskRepository.getAllTasks());

        model.addAttribute("total", taskRepository.countAll());
        model.addAttribute("completed", taskRepository.countByStatus("COMPLETED"));
        model.addAttribute("pending", taskRepository.countByStatus("PENDING"));
        model.addAttribute("overdue", taskRepository.countByStatus("OVERDUE"));

        return "dashboard";
    }

    @GetMapping("/filter")
    public String filterTasks(@RequestParam String status,
                              HttpSession session,
                              org.springframework.ui.Model model) {

        if (session.getAttribute("userEmail") == null) {
            return "redirect:/login";
        }

        model.addAttribute("tasks", taskRepository.getTasksByStatus(status));

        model.addAttribute("total", taskRepository.countAll());
        model.addAttribute("completed", taskRepository.countByStatus("COMPLETED"));
        model.addAttribute("pending", taskRepository.countByStatus("PENDING"));
        model.addAttribute("overdue", taskRepository.countByStatus("OVERDUE"));

        return "dashboard";
    }

    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=tasks.csv");

        PrintWriter writer = response.getWriter();

        writer.println("Title,Description,Time,Status");

        for (Task task : taskRepository.getAllTasks()) {
            writer.println(task.getTitle() + "," +
                    task.getDescription() + "," +
                    task.getTime() + "," +
                    task.getStatus());
        }

        writer.flush();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}