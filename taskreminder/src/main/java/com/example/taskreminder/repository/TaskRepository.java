package com.example.taskreminder.repository;

import com.example.taskreminder.entity.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, time, status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                java.sql.Timestamp.valueOf(task.getTime()),
                task.getStatus());
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks ORDER BY id DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setTime(rs.getTimestamp("time").toLocalDateTime());
            task.setStatus(rs.getString("status"));
            return task;
        });
    }

    public List<Task> getTasksByStatus(String status) {
        String sql = "SELECT * FROM tasks WHERE status=? ORDER BY id DESC";
        return jdbcTemplate.query(sql, ps -> ps.setString(1, status), (rs, rowNum) -> {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setTime(rs.getTimestamp("time").toLocalDateTime());
            task.setStatus(rs.getString("status"));
            return task;
        });
    }

    public int countAll() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tasks", Integer.class);
        return count != null ? count : 0;
    }

    public int countByStatus(String status) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tasks WHERE status=?",
                new Object[]{status},
                Integer.class
        );
        return count != null ? count : 0;
    }

    public void updateTask(Long id, String title, String description,
                           java.time.LocalDateTime time, String status) {

        String sql = "UPDATE tasks SET title=?, description=?, time=?, status=? WHERE id=?";

        jdbcTemplate.update(sql,
                title,
                description,
                java.sql.Timestamp.valueOf(time),
                status,
                id);
    }

    public void updateStatus(Long id, String status) {
        jdbcTemplate.update("UPDATE tasks SET status=? WHERE id=?", status, id);
    }

    public void deleteTask(Long id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id=?", id);
    }
}