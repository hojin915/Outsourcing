package com.example.outsourcing.task.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssigneeDto {
    private Long id;
    private String username;
    private String name;
    private String email;
}