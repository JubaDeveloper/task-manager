package io.github.jubadeveloper.actors.web.panel;

import io.github.jubadeveloper.core.domain.User;
import io.github.jubadeveloper.core.meta.annotations.WebLayer;
import io.github.jubadeveloper.core.service.TaskService;
import io.github.jubadeveloper.core.service.UserService;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@WebLayer("/panel/task")
public class Task {
    private final TaskService taskService;
    private final UserService userService;
    public Task (
            TaskService taskService,
            UserService userService
    ) {
        this.taskService = taskService;
        this.userService = userService;
    }
    @GetMapping
    public String taskGet () {
        return "panel/task";
    }

    @PostMapping(params = "create")
    public String createTask (
            @ModelAttribute("task") io.github.jubadeveloper.core.domain.Task task,
            @ModelAttribute("userData") User user,
            @ModelAttribute("user") UserDetails userDetails,
            Model model
    ) {
        System.out.println("Creating task");
        task.setUser(user);
        task.setId(null);
        taskService.createOrUpdateTask(task);
        model.addAttribute("success", "Task created");
        model.addAttribute("tasks", tasks(userDetails, model));
        return "panel/task";
    }

    @PostMapping(params = {"update"})
    public String updateTask (
            io.github.jubadeveloper.core.domain.Task task,
            @ModelAttribute("userData") User user,
            @RequestParam("update") Long taskId,
            @ModelAttribute("user") UserDetails userDetails,
            Model model
    ) {
        io.github.jubadeveloper.core.domain.Task task1 = taskService.loadTaskByIdAndUserEmail(taskId, user.getEmail());
        if (task1 == null) {
            model.addAttribute("error", "Task not found");
            return "panel/task";
        }
        task.setId(taskId);
        task.setUser(user);
        taskService.createOrUpdateTask(task);
        model.addAttribute("success", "Task updated");
        model.addAttribute("tasks", tasks(userDetails, model));
        return "panel/task";
    }

    @PostMapping(params = {"delete"})
    public String deleteTask (
            @ModelAttribute("user") UserDetails user,
            @RequestParam("delete") Long taskId,
            Model model
    )  {
        if (taskService.loadTaskByIdAndUserEmail(taskId, user.getUsername()) == null) {
            model.addAttribute("error", "Task not found");
            return "panel/task";
        }
        taskService.deleteTaskById(taskId);
        model.addAttribute("tasks", tasks(user, model));
        model.addAttribute("success", "Task deleted");
        return "panel/task";
    }

    @ModelAttribute("task")
    public io.github.jubadeveloper.core.domain.Task task (
            @RequestParam("taskId") @Nullable Long taskId,
            @ModelAttribute("user") UserDetails user,
            Model model
    ) {
        if (taskId != null) return taskService.loadTaskByIdAndUserEmail(taskId, user.getUsername());
        return new io.github.jubadeveloper.core.domain.Task();
    }

    @ModelAttribute("tasks")
    public List<io.github.jubadeveloper.core.domain.Task> tasks (
            @ModelAttribute("user") UserDetails userDetails,
            Model model
    ) {
        List<io.github.jubadeveloper.core.domain.Task> tasks = taskService.loadTasksByUserId(userDetails.getUsername());
        if (tasks.isEmpty()) {
            model.addAttribute("success", "There is no task yet");
        }
        return tasks;
    }

    @ModelAttribute("user")
    public UserDetails authenticatedUser (@AuthenticationPrincipal Principal principal) {
        return (UserDetails) (((UsernamePasswordAuthenticationToken) principal).getPrincipal());
    }

    @ModelAttribute("userData")
    public User authenticatedUser (@ModelAttribute("user") UserDetails userDetails) {
        if (userDetails != null) {
            return this.userService.loadByEmail(userDetails.getUsername());
        }
        return null;
    }
}
