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

                // 3) Docentes primero: quedan con id 1 (Ana) y 2 (Luis)
                Usuario ana = usuarioRepository.save(new Usuario(null, "Ana",
                        "ana@colegio.cl", "docente123", Perfil.DOCENTE, null));
                Usuario luis = usuarioRepository.save(new Usuario(null, "Luis",
                        "luis@colegio.cl", "docente123", Perfil.DOCENTE, null));

                // 4) Estudiantes: ids 3 al 7 (el 3 y el 4 están en 5°A)
                usuarioRepository.save(new Usuario(null, "Estudiante Uno",
                        "estudiante1@colegio.cl", "alumno123", Perfil.ESTUDIANTE, quintoA.getId()));
                usuarioRepository.save(new Usuario(null, "Estudiante Dos",
                        "estudiante2@colegio.cl", "alumno123", Perfil.ESTUDIANTE, quintoA.getId()));
                usuarioRepository.save(new Usuario(null, "Estudiante Tres",
                        "estudiante3@colegio.cl", "alumno123", Perfil.ESTUDIANTE, quintoA.getId()));
                usuarioRepository.save(new Usuario(null, "Estudiante Cuatro",
                        "estudiante4@colegio.cl", "alumno123", Perfil.ESTUDIANTE, quintoA.getId()));
                usuarioRepository.save(new Usuario(null, "Estudiante Cinco",
                        "estudiante5@colegio.cl", "alumno123", Perfil.ESTUDIANTE, sextoB.getId()));

                // 5) Asignaciones: 1 = Ana 5°A Matemática, 2 = Ana 5°A Lenguaje, 3 = Luis 6°B Historia
                asignacionRepository.save(new Asignacion(null, ana.getId(), quintoA.getId(), matematica.getId()));
                asignacionRepository.save(new Asignacion(null, ana.getId(), quintoA.getId(), lenguaje.getId()));
                asignacionRepository.save(new Asignacion(null, luis.getId(), sextoB.getId(), historia.getId()));
            }
        };
    }
}
