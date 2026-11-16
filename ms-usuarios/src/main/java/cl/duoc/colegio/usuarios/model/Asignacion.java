package cl.duoc.colegio.usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Une un docente con un curso y una asignatura (ej: Ana, 5°A, Matemática).
// ms-bitacoras guarda el id de esta asignación en cada bitácora.
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asignaciones")
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Long id;

    @Column(nullable = false)
    private Long docenteId;

    @Column(nullable = false)
    private Long cursoId;

    @Column(nullable = false)
    private Long asignaturaId;
}
