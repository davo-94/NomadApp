package cl.vasquez.nomadapp.controller;

import cl.vasquez.nomadapp.dto.PostRequest;
import cl.vasquez.nomadapp.entity.Post;
import cl.vasquez.nomadapp.service.PostService;
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
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/public")
    public ResponseEntity<?> getPublicPosts() {
        try {
            List<Post> posts = postService.getPublicPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener posts: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getPublicPosts2() {
        return getPublicPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            Optional<Post> post = postService.getPostById(id);
            if (post.isPresent()) {
                return ResponseEntity.ok(post.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener post: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest request,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body("Errores de validación: " + bindingResult.getFieldErrors());
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = extractUserIdFromUsername(username);
            Post post = postService.createPost(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear post: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
                                        @Valid @RequestBody PostRequest request,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body("Errores de validación: " + bindingResult.getFieldErrors());
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = extractUserIdFromUsername(username);
            Post post = postService.updatePost(id, request, userId);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar post: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = extractUserIdFromUsername(username);
            postService.deletePost(id, userId);
            return ResponseEntity.ok("Post eliminado exitosamente");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar post: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> likePost(@PathVariable Long id) {
        try {
            Post post = postService.likePost(id);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al dar like: " + e.getMessage());
        }
    }

    private Long extractUserIdFromUsername(String username) {
        // Implementar lógica para extraer el ID del usuario desde el nombre de usuario
        // Por ahora, retornar un valor por defecto (se debe mejorar)
        return 1L;
    }
}
