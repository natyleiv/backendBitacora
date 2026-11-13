package cl.duoc.colegio.bitacoras.repository;

import cl.duoc.colegio.bitacoras.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {

    // Aportes de una bitácora en un estado (el docente ve solo los ENVIADO), los más nuevos primero
    List<Registro> findByBitacoraIdAndEstadoEnvioOrderByFechaCreacionDesc(Long bitacoraId, String estadoEnvio);

    // Historial de un estudiante (borradores y enviados), los más nuevos primero
    List<Registro> findByEstudianteIdOrderByFechaCreacionDesc(Long estudianteId);
}