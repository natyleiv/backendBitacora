package cl.duoc.colegio.usuarios.service;

import cl.duoc.colegio.usuarios.model.Asignatura;
import cl.duoc.colegio.usuarios.repository.AsignacionRepository;
import cl.duoc.colegio.usuarios.repository.AsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignaturaService {
    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private AsignacionRepository asignacionRepository;

    // 1. Listar todas
    public List<Asignatura> findAll() {
        return asignaturaRepository.findAll();
    }

    // 2. Buscar por ID
    public Asignatura buscarPorId(Long id) {
        return asignaturaRepository.findById(id).orElse(null);
    }

    // 3. Validar los datos. Devuelve el mensaje de error, o null si está todo bien.
    public String validar(Asignatura asignatura) {
        if (asignatura.getNombre() == null || asignatura.getNombre().trim().length() < 3) {
            return "El nombre de la asignatura debe tener al menos 3 caracteres";
        }
        if (asignatura.getNombre().trim().length() > 50) {
            return "El nombre de la asignatura no puede superar los 50 caracteres";
        }
        return null;
    }

    // 4. Regla: no puede haber dos asignaturas con el mismo nombre
    public boolean existeNombre(String nombre, Long idExcluir) {
        if (idExcluir == null) {
            return asignaturaRepository.existsByNombreIgnoreCase(nombre.trim());
        }
        return asignaturaRepository.existsByNombreIgnoreCaseAndIdNot(nombre.trim(), idExcluir);
    }

    // 5. Registrar
    public Asignatura registrar(Asignatura asignatura) {
        asignatura.setId(null);
        asignatura.setNombre(asignatura.getNombre().trim());
        return asignaturaRepository.save(asignatura);
    }

    // 6. Actualizar
    public Asignatura actualizar(Asignatura actual, Asignatura datos) {
        actual.setNombre(datos.getNombre().trim());
        return asignaturaRepository.save(actual);
    }

    // 7. Regla: una asignatura con asignaciones no se puede borrar
    public boolean estaEnUso(Long id) {
        return asignacionRepository.existsByAsignaturaId(id);
    }

    // 8. Borrar
    public boolean borrar(Long id) {
        if (asignaturaRepository.existsById(id)) {
            asignaturaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
