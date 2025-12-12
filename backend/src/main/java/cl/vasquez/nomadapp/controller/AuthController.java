package cl.vasquez.nomadapp.controller;

import cl.vasquez.nomadapp.dto.AuthResponse;
import cl.vasquez.nomadapp.dto.LoginRequest;
import cl.vasquez.nomadapp.dto.RegisterRequest;
import cl.vasquez.nomadapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Errores de validación: " + 
                    bindingResult.getFieldErrors());
        }

        AuthResponse response = authService.register(request);

        if (response.getMessage() != null && 
            (response.getMessage().contains("ya existe") || 
             response.getMessage().contains("registrado"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Errores de validación: " + 
                    bindingResult.getFieldErrors());
        }

        AuthResponse response = authService.login(request);

        if (response.getMessage() != null && 
            (response.getMessage().contains("no encontrado") || 
             response.getMessage().contains("Contraseña") || 
             response.getMessage().contains("deshabilitado"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
