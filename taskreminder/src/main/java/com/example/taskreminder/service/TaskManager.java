package com.example.taskreminder.service;

import com.example.taskreminder.model.Taskmodel;
import com.example.taskreminder.Repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskManager {

    private final TaskRepository repository;

    public TaskManager(TaskRepository repository) {
        this.repository = repository;
    }

    public void addTask(Taskmodel task){
        repository.addTask(task);
    }

    public List<Taskmodel> getAllTasks(){
        return repository.getTasks();
    }

    public void deleteTask(Long id){
        repository.deleteTask(id);
    }

    public void updateTask(Long id, Taskmodel task){
        repository.updateTask(id, task);
    }
}