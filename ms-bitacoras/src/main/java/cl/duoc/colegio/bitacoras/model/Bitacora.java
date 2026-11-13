package cl.duoc.colegio.bitacoras.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bitacoras")
public class Bitacora {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Long id;

    // Id de la asignación (docente + curso + asignatura) que vive en ms-usuarios.
    // Es solo un número: no hay clave foránea porque está en otra base de datos.
    @Column(nullable = false)
    private Long asignacionId;

    @Column(nullable = false, length = 80)
    private String titulo;

    @Column(nullable = false, length = 300)
    private String proposito;

    @Column(nullable = false, length = 500)
    private String instrucciones;

    @Column(nullable = false)
    private int mes;

    @Column(nullable = false)
    private int anio;

    // BORRADOR, PUBLICADA o CERRADA
    @Column(nullable = false, length = 10)
    private String estado;

    // Fechas como texto "dd/MM/yyyy", igual que en la app
    @Column(length = 10)
    private String fechaPublicacion;

    @Column(length = 10)
    private String fechaCierre;
}
