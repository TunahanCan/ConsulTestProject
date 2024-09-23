package com.example.authservice.service;
import com.example.authservice.domain.Erole;
import com.example.authservice.domain.RoleModel;
import com.example.authservice.domain.UserModel;
import com.example.authservice.domain.payload.AuthRequest;
import com.example.authservice.domain.payload.SignupRequest;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    private Optional<RoleModel> findRoleByName(List<RoleModel> roles, String roleName) {
        return roles.stream()
                .filter(role -> role.getRoleName().equals(roleName))
                .findAny();
    }

    public UserModel register(SignupRequest signUpRequest) {
        UserModel user = new UserModel(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<RoleModel> roles = new HashSet<>();
        List<RoleModel> userRoleListDb = roleRepository.findAll();
        if (strRoles == null || strRoles.isEmpty()) {
            RoleModel userRole = findRoleByName(userRoleListDb, String.valueOf(Erole.ROLE_USER))
                    .orElseThrow(() -> new RuntimeException("Error: User Role is not found."));
            roles.add(userRole);
        } else {
            for (String reqRole : strRoles) {
                RoleModel userRole = findRoleByName(userRoleListDb, "ROLE_" + reqRole).orElse(null);
                if (userRole != null) {
                    roles.add(userRole);
                } else {
                    throw new RuntimeException("Error: Role " + reqRole + " is not found.");
                }
            }
        }

        user.setRoles(roles);
        userRepository.save(user);
        return user;
    }

    public UserModel authenticate(AuthRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }
}