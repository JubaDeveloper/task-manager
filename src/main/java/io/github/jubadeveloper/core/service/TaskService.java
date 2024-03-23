package io.github.jubadeveloper.core.service;

import io.github.jubadeveloper.core.domain.Task;
import io.github.jubadeveloper.core.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    public TaskService (
            TaskRepository taskRepository
    ) {
        this.taskRepository = taskRepository;
    }
    public List<Task> loadTasksByUserId (String email) {
        return taskRepository.findByUserEmail(email);
    }

    public Task loadTaskByIdAndUserEmail (Long id, String email) {
        return taskRepository.findByIdAndUserEmail(id, email);
    }

    public Task createOrUpdateTask (Task task) {
        return taskRepository
                .save(task);
    }

    public void deleteTaskById (Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
