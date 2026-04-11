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

    // ✅ GET ALL TASKS
    public List<Task> getAllTasks() {
        return repository.getAllTasks();
    }

    // ✅ FILTER
    public List<Task> getTasksByStatus(String status) {
        return repository.getTasksByStatus(status);
    }

    // ✅ ADD
    public void addTask(Task task) {
        repository.addTask(task);
    }

    // ✅ DELETE
    public void deleteTask(Long id) {
        repository.deleteTask(id);
    }

    // ✅ UPDATE STATUS
    public void updateStatus(Long id, String status) {
        repository.updateStatus(id, status);
    }

    // ✅ COUNTS
    public int countAll() {
        return repository.countAll();
    }

    public int countByStatus(String status) {
        return repository.countByStatus(status);
    }
}