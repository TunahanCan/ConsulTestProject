package com.example.authservice.domain.payload;


import lombok.*;

import javax.validation.constraints.NotBlank;

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