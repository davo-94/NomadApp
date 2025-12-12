package cl.vasquez.nomadapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    
    @NotBlank(message = "El título es requerido")
    @Size(min = 3, max = 200, message = "El título debe tener entre 3 y 200 caracteres")
    private String title;
    
    @NotBlank(message = "El contenido es requerido")
    @Size(min = 10, message = "El contenido debe tener al menos 10 caracteres")
    private String content;
    
    private String imageUrl;
    
    private Boolean published = true;
}
