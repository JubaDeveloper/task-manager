package io.github.jubadeveloper.actors.web.panel;

import io.github.jubadeveloper.core.domain.User;
import io.github.jubadeveloper.core.meta.annotations.WebLayer;
import io.github.jubadeveloper.core.service.TaskService;
import io.github.jubadeveloper.core.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            @SessionAttribute("user") UserDetails user,
            Model model
    ) {
        System.out.println("Creating task");
        task.setUser(userService.loadByEmail(user.getUsername()));
        task.setId(null);
        taskService.createOrUpdateTask(task);
        model.addAttribute("success", "Task created");
        model.addAttribute("tasks", tasks(user, model));
        return "panel/task";
    }

    @PostMapping(params = {"update"})
    public String updateTask (
            io.github.jubadeveloper.core.domain.Task task,
            @RequestParam("update") Long taskId,
            @SessionAttribute("user") UserDetails user,
            Model model
    ) {
        io.github.jubadeveloper.core.domain.Task task1 = taskService.loadTaskByIdAndUserEmail(taskId, user.getUsername());
        if (task1 == null) {
            model.addAttribute("error", "Task not found");
            return "panel/task";
        }
        task.setId(taskId);
        task.setUser(userService.loadByEmail(user.getUsername()));
        taskService.createOrUpdateTask(task);
        model.addAttribute("success", "Task updated");
        model.addAttribute("tasks", tasks(user, model));
        return "panel/task";
    }

    @PostMapping(params = {"delete"})
    public String deleteTask (
            @SessionAttribute("user") UserDetails user,
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
            @SessionAttribute("user") UserDetails user
    ) {
        if (taskId != null) return taskService.loadTaskByIdAndUserEmail(taskId, user.getUsername());
        return io.github.jubadeveloper.core.domain.Task.builder()
                .description("")
                .title("")
                .finished(false)
                .build();
    }

    @ModelAttribute("tasks")
    public List<io.github.jubadeveloper.core.domain.Task> tasks (
            @SessionAttribute("user") UserDetails user,
            Model model
    ) {
        List<io.github.jubadeveloper.core.domain.Task> tasks = taskService.loadTasksByUserId(user.getUsername());
        if (tasks.isEmpty()) {
            model.addAttribute("success", "There is no task yet");
        }
        return tasks;
    }
}
