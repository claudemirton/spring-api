package com.example.restful.projectmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Data
public class Project {

    @Id
    @GeneratedValue
    private Integer Id;

    @Size(min=3, max=100, message = "Name should have at least 3 chars and 100 at most")
    private String name;

    @FutureOrPresent(message = "The project creation date can't be in the past")
    private LocalDate creationDate;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<Task> tasks;
}
