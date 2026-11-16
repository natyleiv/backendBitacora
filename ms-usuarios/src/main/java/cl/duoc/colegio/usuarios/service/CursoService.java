package cl.duoc.colegio.usuarios.service;

import cl.duoc.colegio.usuarios.model.Curso;
import cl.duoc.colegio.usuarios.repository.AsignacionRepository;
import cl.duoc.colegio.usuarios.repository.CursoRepository;
import cl.duoc.colegio.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AsignacionRepository asignacionRepository;

    // 1. Listar todos
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    // 2. Buscar por ID
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    // 3. Validar los datos. Devuelve el mensaje de error, o null si está todo bien.
    public String validar(Curso curso) {
        if (curso.getNombre() == null || curso.getNombre().trim().length() < 2) {
            return "El nombre del curso debe tener al menos 2 caracteres";
        }
        if (curso.getNombre().trim().length() > 20) {
            return "El nombre del curso no puede superar los 20 caracteres";
        }
        return null;
    }

    // 4. Regla: no puede haber dos cursos con el mismo nombre
    public boolean existeNombre(String nombre, Long idExcluir) {
        if (idExcluir == null) {
            return cursoRepository.existsByNombreIgnoreCase(nombre.trim());
        }
        return cursoRepository.existsByNombreIgnoreCaseAndIdNot(nombre.trim(), idExcluir);
    }

    // 5. Registrar
    public Curso registrar(Curso curso) {
        curso.setId(null);
        curso.setNombre(curso.getNombre().trim());
        return cursoRepository.save(curso);
    }

    // 6. Actualizar
    public Curso actualizar(Curso actual, Curso datos) {
        actual.setNombre(datos.getNombre().trim());
        return cursoRepository.save(actual);
    }

    // 7. Regla: un curso con estudiantes o asignaciones no se puede borrar
    public boolean estaEnUso(Long id) {
        return usuarioRepository.existsByCursoId(id) || asignacionRepository.existsByCursoId(id);
    }

    // 8. Borrar
    public boolean borrar(Long id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
