package com.example.outsourcing.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSummaryResponseDto {
    private Long id;
    private String username;
    private String name;
    private String email;
}
