package com.example.restful.projectmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {

    @Id
    @GeneratedValue
    private Integer id;

    @Size(min=3, max=100, message = "Name should have at least 3 chars and 250 at most")
    private String description;

    @FutureOrPresent(message = "The task start date can't be in the past")
    private LocalDate startDate;

    @FutureOrPresent(message = "The task end date can't be in the past")
    private LocalDate endDate;

    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Project project;

}
