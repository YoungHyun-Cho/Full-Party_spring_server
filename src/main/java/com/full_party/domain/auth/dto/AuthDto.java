package com.full_party.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    private Long userId;
    private String email;
    private String password;
}
