package cl.duoc.colegio.usuarios.service;

import cl.duoc.colegio.usuarios.model.Asignacion;
import cl.duoc.colegio.usuarios.model.Perfil;
import cl.duoc.colegio.usuarios.model.Usuario;
import cl.duoc.colegio.usuarios.repository.AsignacionRepository;
import cl.duoc.colegio.usuarios.repository.AsignaturaRepository;
import cl.duoc.colegio.usuarios.repository.CursoRepository;
import cl.duoc.colegio.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignacionService {
    @Autowired
    private AsignacionRepository asignacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    // 1. Listar todas
    public List<Asignacion> findAll() {
        return asignacionRepository.findAll();
    }

    // 2. Asignaciones de un docente (la app las usa para llamar a ms-bitacoras)
    public List<Asignacion> buscarPorDocente(Long docenteId) {
        return asignacionRepository.findByDocenteId(docenteId);
    }

    // 3. Asignaciones de un curso (lo que ve un estudiante)
    public List<Asignacion> buscarPorCurso(Long cursoId) {
        return asignacionRepository.findByCursoId(cursoId);
    }

    // 4. Buscar por ID
    public Asignacion buscarPorId(Long id) {
        return asignacionRepository.findById(id).orElse(null);
    }

    // 5. Validar los datos. Devuelve el mensaje de error, o null si está todo bien.
    public String validar(Asignacion asignacion) {
        if (asignacion.getDocenteId() == null) {
            return "Seleccione un docente";
        }
        if (asignacion.getCursoId() == null) {
            return "Seleccione un curso";
        }
        if (asignacion.getAsignaturaId() == null) {
            return "Seleccione una asignatura";
        }
        Usuario docente = usuarioRepository.findById(asignacion.getDocenteId()).orElse(null);
        if (docente == null || !Perfil.DOCENTE.equals(docente.getPerfil())) {
            return "El docente indicado no existe";
        }
        if (!cursoRepository.existsById(asignacion.getCursoId())) {
            return "El curso indicado no existe";
        }
        if (!asignaturaRepository.existsById(asignacion.getAsignaturaId())) {
            return "La asignatura indicada no existe";
        }
        return null;
    }

    // 6. Regla: no repetir docente + curso + asignatura
    public boolean existeDuplicada(Asignacion asignacion) {
        return asignacionRepository.existsByDocenteIdAndCursoIdAndAsignaturaId(
                asignacion.getDocenteId(), asignacion.getCursoId(), asignacion.getAsignaturaId());
    }

    // 7. Registrar
    public Asignacion registrar(Asignacion asignacion) {
        asignacion.setId(null);
        return asignacionRepository.save(asignacion);
    }

    // 8. Borrar
    public boolean borrar(Long id) {
        if (asignacionRepository.existsById(id)) {
            asignacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
