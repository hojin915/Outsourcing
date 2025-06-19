package com.example.outsourcing.dashboard.dto;

import com.example.outsourcing.task.entity.Task;
import lombok.Getter;

@Getter
public class TaskByPriorityDto {

    private Long id;
    private String title;
    private String content;
    private Task.Status status;
    private Task.Priority priority;
    private UserDtoForTask user;

    public TaskByPriorityDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.status = task.getStatus();
        this.priority = task.getPriority();
        this.user = new UserDtoForTask(task.getUser());
    }
}