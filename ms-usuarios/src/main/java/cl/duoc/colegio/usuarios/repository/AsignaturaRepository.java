package cl.duoc.colegio.usuarios.repository;

import cl.duoc.colegio.usuarios.model.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    // Regla: no repetir el nombre de una asignatura
    boolean existsByNombreIgnoreCase(String nombre);

    // Lo mismo, pero sin compararse consigo misma (al editar)
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
