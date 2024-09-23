package com.example.authservice.domain.payload;


import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
}