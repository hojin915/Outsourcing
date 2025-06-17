package com.example.outsourcing.dashboard;

import lombok.Getter;

@Getter
public enum TaskPriority {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    TaskPriority(int value) {
    }
}
