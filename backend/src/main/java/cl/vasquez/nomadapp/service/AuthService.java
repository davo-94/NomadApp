package cl.vasquez.nomadapp.service;

import cl.vasquez.nomadapp.dto.AuthResponse;
import cl.vasquez.nomadapp.dto.LoginRequest;
import cl.vasquez.nomadapp.dto.RegisterRequest;
import cl.vasquez.nomadapp.entity.Role;
import cl.vasquez.nomadapp.entity.User;
import cl.vasquez.nomadapp.repository.RoleRepository;
import cl.vasquez.nomadapp.repository.UserRepository;
import cl.vasquez.nomadapp.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthResponse.builder()
                    .message("El username ya existe")
                    .build();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .message("El email ya está registrado")
                    .build();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .build();

        // Asignar rol de usuario regular por defecto
        Role userRole = roleRepository.findByName(Role.RoleEnum.USER)
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name(Role.RoleEnum.USER)
                            .description("Usuario Regular")
                            .build();
                    return roleRepository.save(newRole);
                });

        user.setRoles(new HashSet<>(Set.of(userRole)));
        User savedUser = userRepository.save(user);

        String rolesStr = savedUser.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.joining(","));

        String token = tokenProvider.generateToken(savedUser.getUsername(), savedUser.getEmail(), rolesStr);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .roles(rolesStr)
                .message("Registro exitoso")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            return AuthResponse.builder()
                    .message("Usuario no encontrado")
                    .build();
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .message("Contraseña incorrecta")
                    .build();
        }

        if (!user.getEnabled()) {
            return AuthResponse.builder()
                    .message("Usuario deshabilitado")
                    .build();
        }

        String rolesStr = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.joining(","));

        String token = tokenProvider.generateToken(user.getUsername(), user.getEmail(), rolesStr);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(rolesStr)
                .message("Inicio de sesión exitoso")
                .build();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
