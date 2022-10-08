package com.example.authservice.security;

import com.example.authservice.domain.UserModel;
import com.example.authservice.domain.payload.AuthRequest;
import com.example.authservice.domain.payload.AuthResponse;
import com.example.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Component
@AllArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    private  JwtUtil jwtUtil;

    @Lazy
    private  AuthenticationManager authenticationManager;


    public AuthResponse createJwtToken(AuthRequest jwtRequest) throws Exception{
        String userName = jwtRequest.getUsername();
        String userPassword = jwtRequest.getPassword();
        authenticate(userName, userPassword);
        final UserDetails userDetails = loadUserByUsername(userName);
        String accessToken = jwtUtil.generateAccesToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return new AuthResponse(accessToken, refreshToken);
    }


    public AuthResponse updateJwtTokenProvider(Claims tokenUser) throws Exception{
        final UserDetails userDetails = loadUserByUsername(jwtUtil.getUserNameFromClaims(tokenUser));
        String accessToken = jwtUtil.generateAccesToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), getAuthorities(user));
        } else {
            throw new UsernameNotFoundException("Username is not valid");
        }
    }

    private Set<SimpleGrantedAuthority> getAuthorities(Optional<UserModel> user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.get().getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority( role.getRoleName()));
        });
        return authorities;
    }
    private void authenticate(String userName, String userPassword) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("User is disabled");
        } catch(BadCredentialsException e) {
            throw new Exception("Bad credentials from user");
        }
    }
}