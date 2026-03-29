package com.example.taskreminder.Repository;
import com.example.taskreminder.model.Taskmodel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // ADD TASK
    public void addTask(Taskmodel task){

        String sql = "INSERT INTO tasks (id,title,description,due_date,completed) VALUES (?,?,?,?,?)";

        jdbcTemplate.update(sql,
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus());
    }

    // GET ALL TASKS
    public List<Taskmodel> getTasks(){

        String sql = "SELECT * FROM tasks";

        return jdbcTemplate.query(sql,(rs,rowNum)->{

            Taskmodel task = new Taskmodel();

            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
            task.setStatus(rs.getString("completed"));

            return task;
        });
    }

    // DELETE TASK
    public void deleteTask(Long id){

        String sql = "DELETE FROM tasks WHERE id = ?";

        jdbcTemplate.update(sql,id);
    }

    // UPDATE TASK
    public void updateTask(Long id, Taskmodel task){

        String sql = "UPDATE tasks SET title=?, description=?, due_date=?, completed=? WHERE id=?";

        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                id);
    }
}