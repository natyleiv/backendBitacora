package cl.duoc.colegio.usuarios.repository;

import cl.duoc.colegio.usuarios.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Regla: no repetir el nombre de un curso
    boolean existsByNombreIgnoreCase(String nombre);

    // Lo mismo, pero sin compararse consigo mismo (al editar)
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
