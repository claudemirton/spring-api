package com.example.restful.projectmanagement.controller;

import com.example.restful.projectmanagement.domain.Project;
import com.example.restful.projectmanagement.domain.Task;
import com.example.restful.projectmanagement.exception.UserNotFoundException;
import com.example.restful.projectmanagement.repository.ProjectRepository;
import com.example.restful.projectmanagement.repository.TaskRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskController {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;

    //Retrieve all the tasks
    @GetMapping("/api/tasks")
    public List<Task> retrieveAllTasks(){
        return taskRepository.findAll();
    }

    //Retrieve a single task
    @GetMapping("/api/tasks/{id}")
    public EntityModel<Task> retrieveTask(@PathVariable int id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty()) {
            throw new UserNotFoundException("id: " +id);
        }

        EntityModel<Task> entityModel = EntityModel.of(task.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllTasks());
        entityModel.add(link.withRel("all-tasks"));

        return entityModel;
    }

    //Retrieve tasks assigned to a project
    @GetMapping("/api/projects/{id}/tasks")
    public List<Task> retrieveTaskFromProject(@PathVariable int id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            throw new UserNotFoundException("id: " +id);
        }

        List<Task> taskList = project.get().getTasks();

        return taskList;
    }

    //Edit a task
    @PutMapping("/api/tasks/{id}")
    public EntityModel<Task> updateTask(@PathVariable int id, @Valid @RequestBody Task task) {
        Optional<Task> taskToBeUpdatedOptional = taskRepository.findById(id);
        Task taskToBeUpdated = taskToBeUpdatedOptional.get();

        if (taskToBeUpdatedOptional.isEmpty()) {
            throw new UserNotFoundException("id: " +id);
        }
        taskToBeUpdated.setDescription(task.getDescription());
        taskToBeUpdated.setStartDate(task.getStartDate());
        taskToBeUpdated.setEndDate(task.getEndDate());
        taskToBeUpdated.setCompleted(task.getCompleted());
        taskRepository.save(taskToBeUpdated);

        EntityModel<Task> entityModel = EntityModel.of(taskToBeUpdated);
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllTasks());
        entityModel.add(link.withRel("all-tasks"));

        return entityModel;
    }

    //Removing a task
    @DeleteMapping("/api/tasks/{id}")
    public void deleteTask(@PathVariable int id) {
        taskRepository.deleteById(id);
    }

    //Create a task associated to a project
    @PostMapping("/api/projects/{id}/tasks")
    public ResponseEntity<Task> createPostForUser(@PathVariable int id, @Valid @RequestBody Task task) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            throw new UserNotFoundException("Project not found -  id: " +id);
        }

        task.setProject(project.get());

        Task savedTask = taskRepository.save(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedTask.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
