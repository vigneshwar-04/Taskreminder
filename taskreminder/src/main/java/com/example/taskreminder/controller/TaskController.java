package com.example.taskreminder.controller;

import com.example.taskreminder.model.Taskmodel;
import com.example.taskreminder.service.TaskManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskManager service;

    public TaskController(TaskManager service) {
        this.service = service;
    }

    @GetMapping
    public List<Taskmodel> getAllTasks() {
        return service.getAllTasks();
    }

    @PostMapping
    public String addTask(@RequestBody Taskmodel task) {
        service.addTask(task);
        return "Task added successfully";
    }

    @PutMapping("/{id}")
    public String updateTask(@PathVariable Long id,
                             @RequestBody Taskmodel task) {
        service.updateTask(id, task);
        return "Task updated successfully";
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return "Task deleted successfully";
    }
}