package cl.duoc.colegio.bitacoras.repository;

import cl.duoc.colegio.bitacoras.model.Retroalimentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetroalimentacionRepository extends JpaRepository<Retroalimentacion, Long> {

    // El comentario de un registro (puede no existir todavía)
    Optional<Retroalimentacion> findByRegistroId(Long registroId);
}
