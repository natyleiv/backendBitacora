package cl.duoc.colegio.usuarios.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, unique = true, length = 80)
    private String correo;

    // WRITE_ONLY: la contraseña se puede recibir en el JSON, pero nunca se devuelve
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 60)
    private String password;

    // DOCENTE o ESTUDIANTE
    @Column(nullable = false, length = 10)
    private String perfil;

    // Curso del estudiante (id de la tabla cursos). Los docentes lo dejan en null.
    private Long cursoId;
}
