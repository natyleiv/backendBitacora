package cl.duoc.colegio.usuarios.repository;

import cl.duoc.colegio.usuarios.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    // Lo que usa la app: asignaciones de un docente
    List<Asignacion> findByDocenteId(Long docenteId);

    // Lo que usa la app: asignaciones de un curso (para el estudiante)
    List<Asignacion> findByCursoId(Long cursoId);

    // Regla: no repetir la misma combinación docente + curso + asignatura
    boolean existsByDocenteIdAndCursoIdAndAsignaturaId(Long docenteId, Long cursoId, Long asignaturaId);

    // Para no borrar cursos / asignaturas / docentes que tienen asignaciones
    boolean existsByCursoId(Long cursoId);

    boolean existsByAsignaturaId(Long asignaturaId);

    boolean existsByDocenteId(Long docenteId);
}
