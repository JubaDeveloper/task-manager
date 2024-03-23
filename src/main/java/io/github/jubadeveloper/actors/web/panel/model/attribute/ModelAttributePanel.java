package io.github.jubadeveloper.actors.web.panel.model.attribute;

import io.github.jubadeveloper.core.domain.Task;
import io.github.jubadeveloper.core.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@Component
public class ModelAttributePanel {
    private final TaskService taskService;
    public ModelAttributePanel (
            TaskService taskService
    ) {
        this.taskService = taskService;
    }
    @ModelAttribute("tasks")
    public List<Task> tasks (@AuthenticationPrincipal Principal principal) {
        System.out.println("Getting tasks");
        return taskService.loadTasksByUserId(principal.getName());
    }
}
