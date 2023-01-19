package com.example.restful.projectmanagement.controller;

import com.example.restful.projectmanagement.domain.Project;
import com.example.restful.projectmanagement.exception.UserNotFoundException;
import com.example.restful.projectmanagement.repository.ProjectRepository;
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
public class ProjectController {

    private ProjectRepository projectRepository;

    //Retrieve all projects
    @GetMapping("/api/projects")
    public List<Project> retrieveAllProjects(){
        return projectRepository.findAll();
    }

    //Retrieve a single project
    @GetMapping("/api/projects/{id}")
    public EntityModel<Project> retrieveProject(@PathVariable int id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            throw new UserNotFoundException("id: " +id);
        }

        EntityModel<Project> entityModel = EntityModel.of(project.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllProjects());
        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }

    //Edit a project
    @PutMapping("/api/projects/{id}")
    public EntityModel<Project> updateProject(@PathVariable int id, @Valid @RequestBody Project project) {
        Optional<Project> projectToBeUpdatedOptional = projectRepository.findById(id);
        Project projectToBeUpdated = projectToBeUpdatedOptional.get();

        if (projectToBeUpdatedOptional.isEmpty()) {
            throw new UserNotFoundException("id: " +id);
        }
        projectToBeUpdated.setName(project.getName());
        projectToBeUpdated.setCreationDate(project.getCreationDate());
        projectRepository.save(projectToBeUpdated);

        EntityModel<Project> entityModel = EntityModel.of(projectToBeUpdated);
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllProjects());
        entityModel.add(link.withRel("all-users"));

        return entityModel;
    }

    //Removing a project
    @DeleteMapping("/api/projects/{id}")
    public void deleteProject(@PathVariable int id) {
        projectRepository.deleteById(id);
    }

    @PostMapping("/api/projects")
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) {
        Project savedProject = projectRepository.save(project);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProject.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
