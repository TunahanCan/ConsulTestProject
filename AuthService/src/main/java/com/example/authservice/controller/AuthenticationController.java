package com.example.authservice.controller;
import com.example.authservice.domain.UserModel;
import com.example.authservice.domain.payload.AuthRequest;
import com.example.authservice.domain.payload.AuthResponse;
import com.example.authservice.domain.payload.SignupRequest;
import com.example.authservice.service.AuthenticationService;
import com.example.authservice.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody SignupRequest registerUserDto) {
        UserModel registeredUser = authenticationService.register(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest loginUserDto) {
        UserModel authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(jwtToken);
        authResponse.setRefreshToken(jwtToken);
        authResponse.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(authResponse);
    }
}