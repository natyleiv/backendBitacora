package cl.duoc.colegio.bitacoras.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "retroalimentaciones")
public class Retroalimentacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_retroalimentacion")
    private Long id;

    // Registro comentado. unique = true: un registro tiene como máximo UN comentario
    @Column(nullable = false, unique = true)
    private Long registroId;

    // Docente que comentó (vive en ms-usuarios)
    @Column(nullable = false)
    private Long docenteId;

    @Column(nullable = false, length = 500)
    private String comentario;

    // Momento del comentario en milisegundos, igual que en la app
    private Long fecha;
}
