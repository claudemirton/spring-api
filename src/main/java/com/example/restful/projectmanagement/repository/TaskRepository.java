package com.example.restful.projectmanagement.repository;

import com.example.restful.projectmanagement.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Integer> {
}
