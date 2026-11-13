package cl.duoc.colegio.bitacoras.service;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import cl.duoc.colegio.bitacoras.model.EstadoBitacora;
import cl.duoc.colegio.bitacoras.repository.BitacoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BitacoraService {
    @Autowired
    private BitacoraRepository bitacoraRepository;

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // 1. Listar todas
    public List<Bitacora> findAll() {
        return bitacoraRepository.findAll();
    }

    // 2. Listar las de una asignación (lo que ve un docente)
    public List<Bitacora> buscarPorAsignacion(Long asignacionId) {
        return bitacoraRepository.findByAsignacionId(asignacionId);
    }

    // 3. Buscar por ID
    public Bitacora buscarPorId(Long id) {
        return bitacoraRepository.findById(id).orElse(null);
    }

    // 4. Validar los datos. Devuelve el mensaje de error, o null si está todo bien.
    //    Son las mismas reglas que valida la app.
    public String validar(Bitacora bitacora) {
        if (bitacora.getAsignacionId() == null) {
            return "Seleccione un curso y asignatura";
        }
        if (bitacora.getTitulo() == null || bitacora.getTitulo().trim().length() < 5) {
            return "El título debe tener al menos 5 caracteres";
        }
        if (bitacora.getProposito() == null || bitacora.getProposito().isBlank()) {
            return "El propósito es obligatorio";
        }
        if (bitacora.getInstrucciones() == null || bitacora.getInstrucciones().isBlank()) {
            return "Las instrucciones son obligatorias";
        }
        if (bitacora.getMes() < 1 || bitacora.getMes() > 12) {
            return "El mes debe estar entre 1 y 12";
        }
        if (bitacora.getAnio() < 2025 || bitacora.getAnio() > 2100) {
            return "El año debe estar entre 2025 y 2100";
        }
        return null;
    }

    // 5. Regla: no puede haber dos bitácoras del mismo mes para la misma asignación
    public boolean existeDuplicada(Bitacora bitacora, Long idExcluir) {
        if (idExcluir == null) {
            return bitacoraRepository.existsByAsignacionIdAndMesAndAnio(
                    bitacora.getAsignacionId(), bitacora.getMes(), bitacora.getAnio());
        }
        return bitacoraRepository.existsByAsignacionIdAndMesAndAnioAndIdNot(
                bitacora.getAsignacionId(), bitacora.getMes(), bitacora.getAnio(), idExcluir);
    }

    // 6. Registrar: toda bitácora nueva parte como borrador
    public Bitacora registrar(Bitacora bitacora) {
        bitacora.setId(null);
        bitacora.setEstado(EstadoBitacora.BORRADOR);
        bitacora.setFechaPublicacion(null);
        bitacora.setFechaCierre(null);
        return bitacoraRepository.save(bitacora);
    }

    // 7. Actualizar (el controller revisa antes que esté en borrador)
    public Bitacora actualizar(Bitacora actual, Bitacora datos) {
        actual.setAsignacionId(datos.getAsignacionId());
        actual.setTitulo(datos.getTitulo());
        actual.setProposito(datos.getProposito());
        actual.setInstrucciones(datos.getInstrucciones());
        actual.setMes(datos.getMes());
        actual.setAnio(datos.getAnio());
        return bitacoraRepository.save(actual);
    }

    // 8. Publicar: BORRADOR -> PUBLICADA
    public Bitacora publicar(Bitacora bitacora) {
        bitacora.setEstado(EstadoBitacora.PUBLICADA);
        bitacora.setFechaPublicacion(LocalDate.now().format(FORMATO));
        return bitacoraRepository.save(bitacora);
    }

    // 9. Cerrar: PUBLICADA -> CERRADA
    public Bitacora cerrar(Bitacora bitacora) {
        bitacora.setEstado(EstadoBitacora.CERRADA);
        bitacora.setFechaCierre(LocalDate.now().format(FORMATO));
        return bitacoraRepository.save(bitacora);
    }

    // 10. Borrar
    public boolean borrar(Long id) {
        if (bitacoraRepository.existsById(id)) {
            bitacoraRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
