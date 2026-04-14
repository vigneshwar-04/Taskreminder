package com.example.taskreminder.service;

import com.example.taskreminder.entity.Task;
import com.example.taskreminder.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskManager {

    private final TaskRepository repository;

    public TaskManager(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getTasksByEmail(String email) {
        return repository.getTasksByEmail(email);
    }

    public List<Task> getTasksByStatus(String email, String status) {
        return repository.getTasksByStatus(email, status);
    }

    public void addTask(Task task, String email) {
        repository.addTask(task, email);
    }

    public void deleteTask(Long id) {
        repository.deleteTask(id);
    }

    public void updateStatus(Long id, String status) {
        repository.updateStatus(id, status);
    }

    public int countAll(String email) {
        return repository.countAll(email);
    }

    public int countByStatus(String email, String status) {
        return repository.countByStatus(email, status);
    }
}