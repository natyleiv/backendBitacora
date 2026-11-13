package cl.duoc.colegio.bitacoras.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preguntas_orientadoras")
public class PreguntaOrientadora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Long id;

    // Id de la bitácora a la que pertenece la pregunta
    @Column(nullable = false)
    private Long bitacoraId;

    // Posición de la pregunta (1, 2, 3...) para mostrarlas en orden
    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false, length = 200)
    private String texto;
}