package cl.vasquez.nomadapp.controller;

import cl.vasquez.nomadapp.dto.ContactRequest;
import cl.vasquez.nomadapp.entity.Contact;
import cl.vasquez.nomadapp.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createContact(@Valid @RequestBody ContactRequest request,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body("Errores de validación: " + bindingResult.getFieldErrors());
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = extractUserIdFromUsername(username);
            Contact contact = contactService.createContact(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(contact);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear contacto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getContactById(@PathVariable Long id) {
        try {
            Optional<Contact> contact = contactService.getContactById(id);
            if (contact.isPresent()) {
                return ResponseEntity.ok(contact.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Contacto no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener contacto: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyContacts() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = extractUserIdFromUsername(username);
            List<Contact> contacts = contactService.getUserContacts(userId);
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener contactos: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateContact(@PathVariable Long id,
                                           @Valid @RequestBody ContactRequest request,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body("Errores de validación: " + bindingResult.getFieldErrors());
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = extractUserIdFromUsername(username);
            Contact contact = contactService.updateContact(id, request, userId);
            return ResponseEntity.ok(contact);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar contacto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = extractUserIdFromUsername(username);
            contactService.deleteContact(id, userId);
            return ResponseEntity.ok("Contacto eliminado exitosamente");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar contacto: " + e.getMessage());
        }
    }

    private Long extractUserIdFromUsername(String username) {
        // Implementar lógica para extraer el ID del usuario desde el nombre de usuario
        // Por ahora, retornar un valor por defecto (se debe mejorar)
        return 1L;
    }
}
