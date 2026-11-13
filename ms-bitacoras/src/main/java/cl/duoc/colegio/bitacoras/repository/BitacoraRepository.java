package cl.duoc.colegio.bitacoras.repository;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long> {

    // Bitácoras de una asignación (lo que ve un docente)
    List<Bitacora> findByAsignacionId(Long asignacionId);

    // Regla: no repetir bitácora del mismo mes para la misma asignación
    boolean existsByAsignacionIdAndMesAndAnio(Long asignacionId, int mes, int anio);

    // Lo mismo, pero sin compararse consigo misma (al editar)
    boolean existsByAsignacionIdAndMesAndAnioAndIdNot(Long asignacionId, int mes, int anio, Long id);
}
