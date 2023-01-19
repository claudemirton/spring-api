package com.example.restful.projectmanagement.repository;

import com.example.restful.projectmanagement.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Integer> {
}
