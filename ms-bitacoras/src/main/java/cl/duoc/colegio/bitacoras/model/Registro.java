package cl.duoc.colegio.bitacoras.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registros")
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Long id;

    // Bitácora a la que pertenece el registro
    @Column(nullable = false)
    private Long bitacoraId;

    // Id del estudiante (vive en ms-usuarios)
    @Column(nullable = false)
    private Long estudianteId;

    // Fecha de la clase como texto "dd/MM/yyyy", igual que en la app
    @Column(nullable = false, length = 10)
    private String fechaClase;

    @Column(nullable = false, length = 500)
    private String contenido;

    @Column(length = 500)
    private String actividad;

    @Column(length = 500)
    private String aprendizaje;

    @Column(length = 500)
    private String dudas;

    @Column(length = 300)
    private String fotoUri;

    @Column(length = 300)
    private String enlace;

    // BORRADOR o ENVIADO
    @Column(nullable = false, length = 10)
    private String estadoEnvio;

    // Momentos en milisegundos, igual que en la app (System.currentTimeMillis())
    private Long fechaCreacion;

    private Long fechaEnvio;
}
