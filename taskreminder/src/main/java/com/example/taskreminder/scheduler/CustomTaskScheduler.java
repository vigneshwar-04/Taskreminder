package com.example.taskreminder.scheduler;

import com.example.taskreminder.entity.Task;
import com.example.taskreminder.repository.TaskRepository;
import com.example.taskreminder.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CustomTaskScheduler {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    public CustomTaskScheduler(TaskRepository taskRepository,
                               EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkTasks() {

        List<Task> tasks = taskRepository.getAllTasks();

        for (Task task : tasks) {

            if (task.getTime() == null) continue;

            if (task.getTime().isBefore(LocalDateTime.now()) &&
                    !"COMPLETED".equalsIgnoreCase(task.getStatus())) {

                taskRepository.updateStatus(task.getId(), "OVERDUE");

                emailService.sendEmail(
                        "your_email@gmail.com",
                        "Task Overdue",
                        "Task '" + task.getTitle() + "' is overdue!"
                );

            } else if (task.getTime().isAfter(LocalDateTime.now()) &&
                    !"COMPLETED".equalsIgnoreCase(task.getStatus())) {

                taskRepository.updateStatus(task.getId(), "PENDING");

                emailService.sendEmail(
                        "your_email@gmail.com",
                        "Task Pending",
                        "Task '" + task.getTitle() + "' is still pending."
                );
            }
        }
    }
}