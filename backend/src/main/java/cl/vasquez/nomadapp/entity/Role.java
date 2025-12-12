package cl.vasquez.nomadapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "seq_role_id", allocationSize = 1)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
    
    @Column(columnDefinition = "CLOB")
    private String description;
    
    public enum RoleEnum {
        ADMIN("Administrador"),
        USER("Usuario Regular"),
        MODERATOR("Moderador");
        
        private final String displayName;
        
        RoleEnum(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
