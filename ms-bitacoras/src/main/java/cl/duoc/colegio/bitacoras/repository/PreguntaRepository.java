package cl.duoc.colegio.bitacoras.repository;

import cl.duoc.colegio.bitacoras.model.PreguntaOrientadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<PreguntaOrientadora, Long> {

    // Preguntas de una bitácora, ordenadas por su número (1, 2, 3...)
    List<PreguntaOrientadora> findByBitacoraIdOrderByOrdenAsc(Long bitacoraId);

    // Borra todas las preguntas de una bitácora (al editarlas o al borrar la bitácora)
    void deleteByBitacoraId(Long bitacoraId);
}
