package cl.duoc.colegio.usuarios.config;

import cl.duoc.colegio.usuarios.model.Asignacion;
import cl.duoc.colegio.usuarios.model.Asignatura;
import cl.duoc.colegio.usuarios.model.Curso;
import cl.duoc.colegio.usuarios.model.Perfil;
import cl.duoc.colegio.usuarios.model.Usuario;
import cl.duoc.colegio.usuarios.repository.AsignacionRepository;
import cl.duoc.colegio.usuarios.repository.AsignaturaRepository;
import cl.duoc.colegio.usuarios.repository.CursoRepository;
import cl.duoc.colegio.usuarios.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Datos de prueba. IMPORTANTE: los ids se asignan en el orden en que se guarda,
// y ms-bitacoras ya usa estos ids (docentes 1 y 2, estudiantes 3 al 7,
// asignaciones 1, 2 y 3). No cambiar el orden.
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosDemo(CursoRepository cursoRepository,
                                      AsignaturaRepository asignaturaRepository,
                                      UsuarioRepository usuarioRepository,
                                      AsignacionRepository asignacionRepository) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                // 1) Cursos (save devuelve el curso ya con su id)
                Curso quintoA = cursoRepository.save(new Curso(null, "5°A"));
                Curso sextoB = cursoRepository.save(new Curso(null, "6°B"));

                // 2) Asignaturas
                Asignatura matematica = asignaturaRepository.save(new Asignatura(null, "Matemática"));
                Asignatura lenguaje = asignaturaRepository.save(new Asignatura(null, "Lenguaje"));
                Asignatura historia = asignaturaRepository.save(new Asignatura(null, "Historia"));

                // 3) Docentes primero: quedan con id 1 (Christian Acuña) y 2 (Luis Soto)
                //    Mismos nombres y correos que DatosDemo.kt de la app
                Usuario christian = usuarioRepository.save(new Usuario(null, "Profesor Christian Acuña",
                        "docente1@escuela.cl", "docente123", Perfil.DOCENTE, null));
                Usuario luis = usuarioRepository.save(new Usuario(null, "Profesor Luis Soto",
                        "docente2@escuela.cl", "docente123", Perfil.DOCENTE, null));

                // 4) Estudiantes: ids 3 al 7 (del 3 al 6 en 5°A, el 7 en 6°B)
                usuarioRepository.save(new Usuario(null, "Estudiante Uno",
                        "alumno1@escuela.cl", "alumno123", Perfil.ESTUDIANTE, quintoA.getId()));
                usuarioRepository.save(new Usuario(null, "Estudiante Dos",
                        "alumno2@escuela.cl", "alumno123", Perfil.ESTUDIANTE, quintoA.getId()));
                usuarioRepository.save(new Usuario(null, "Estudiante Tres",
                        "alumno3@escuela.cl", "alumno123", Perfil.ESTUDIANTE, quintoA.getId()));
                usuarioRepository.save(new Usuario(null, "Estudiante Cuatro",
                        "alumno4@escuela.cl", "alumno123", Perfil.ESTUDIANTE, quintoA.getId()));
                usuarioRepository.save(new Usuario(null, "Estudiante Cinco",
                        "alumno5@escuela.cl", "alumno123", Perfil.ESTUDIANTE, sextoB.getId()));

                // 5) Asignaciones: 1 = Christian 5°A Matemática, 2 = Christian 5°A Lenguaje, 3 = Luis 6°B Historia
                asignacionRepository.save(new Asignacion(null, christian.getId(), quintoA.getId(), matematica.getId()));
                asignacionRepository.save(new Asignacion(null, christian.getId(), quintoA.getId(), lenguaje.getId()));
                asignacionRepository.save(new Asignacion(null, luis.getId(), sextoB.getId(), historia.getId()));
            }
        };
    }
}
