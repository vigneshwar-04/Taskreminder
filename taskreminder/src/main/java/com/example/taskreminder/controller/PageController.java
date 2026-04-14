package com.example.taskreminder.controller;

import com.example.taskreminder.entity.User;
import com.example.taskreminder.repository.UserRepository;
import com.example.taskreminder.service.EmailService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class PageController {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public PageController(UserRepository userRepository,
                          EmailService emailService) {
        this.userRepository = userRepository;
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
    public String loginPage(HttpSession session, Model model) {

        Object showReset = session.getAttribute("showReset");

        if (showReset != null) {
            model.addAttribute("showReset", true);
            session.removeAttribute("showReset");
        }

        return "login";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return "redirect:/login";
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
                return "redirect:/tasks/dashboard";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/forgot")
    public String forgotPassword(@RequestParam String email,
                                 HttpSession session,
                                 org.springframework.ui.Model model) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "login";
        }

        String otp = String.valueOf((int)(Math.random() * 9000) + 1000);

        session.setAttribute("resetOtp", otp);
        session.setAttribute("resetEmail", email);

        emailService.sendOTP(email, otp);

        model.addAttribute("showReset", true);

        return "login";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String otp,
                                @RequestParam String newPassword,
                                HttpSession session) {

        String sessionOtp = (String) session.getAttribute("resetOtp");
        String email = (String) session.getAttribute("resetEmail");

        if (sessionOtp != null && sessionOtp.equals(otp)) {

            Optional<User> userOpt = userRepository.findByEmail(email);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setPassword(newPassword);
                userRepository.save(user);
            }

            session.removeAttribute("resetOtp");
            session.removeAttribute("resetEmail");

            return "redirect:/login";
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}