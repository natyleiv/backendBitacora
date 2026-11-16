package cl.duoc.colegio.usuarios.service;

import cl.duoc.colegio.usuarios.dto.LoginRequest;
import cl.duoc.colegio.usuarios.dto.LoginResponse;
import cl.duoc.colegio.usuarios.model.Perfil;
import cl.duoc.colegio.usuarios.model.Usuario;
import cl.duoc.colegio.usuarios.repository.AsignacionRepository;
import cl.duoc.colegio.usuarios.repository.CursoRepository;
import cl.duoc.colegio.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AsignacionRepository asignacionRepository;

    // 1. Listar todos
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    // 2. Listar los estudiantes de un curso
    public List<Usuario> buscarPorCurso(Long cursoId) {
        return usuarioRepository.findByCursoId(cursoId);
    }

    // 3. Buscar por ID
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // 4. Validar los datos. Devuelve el mensaje de error, o null si está todo bien.
    //    Son las mismas reglas que valida la app.
    public String validar(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().length() < 3) {
            return "El nombre debe tener al menos 3 caracteres";
        }
        if (usuario.getCorreo() == null || !usuario.getCorreo().contains("@")
                || !usuario.getCorreo().trim().toLowerCase().endsWith(".cl")) {
            return "El correo debe contener @ y terminar en .cl";
        }
        if (usuario.getPassword() == null || usuario.getPassword().length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres";
        }
        if (!Perfil.DOCENTE.equals(usuario.getPerfil()) && !Perfil.ESTUDIANTE.equals(usuario.getPerfil())) {
            return "El perfil debe ser DOCENTE o ESTUDIANTE";
        }
        // Un estudiante siempre pertenece a un curso que exista
        if (Perfil.ESTUDIANTE.equals(usuario.getPerfil())) {
            if (usuario.getCursoId() == null) {
                return "El estudiante debe tener un curso";
            }
            if (!cursoRepository.existsById(usuario.getCursoId())) {
                return "El curso indicado no existe";
            }
        }
        return null;
    }

    // 5. Regla: el correo no se puede repetir
    public boolean existeCorreo(String correo, Long idExcluir) {
        if (idExcluir == null) {
            return usuarioRepository.existsByCorreoIgnoreCase(correo.trim());
        }
        return usuarioRepository.existsByCorreoIgnoreCaseAndIdNot(correo.trim(), idExcluir);
    }

    // 6. Registrar
    public Usuario registrar(Usuario usuario) {
        usuario.setId(null);
        usuario.setCorreo(usuario.getCorreo().trim());
        // Los docentes no pertenecen a un curso
        if (Perfil.DOCENTE.equals(usuario.getPerfil())) {
            usuario.setCursoId(null);
        }
        return usuarioRepository.save(usuario);
    }

    // 7. Actualizar
    public Usuario actualizar(Usuario actual, Usuario datos) {
        actual.setNombre(datos.getNombre());
        actual.setCorreo(datos.getCorreo().trim());
        actual.setPassword(datos.getPassword());
        actual.setPerfil(datos.getPerfil());
        actual.setCursoId(Perfil.DOCENTE.equals(datos.getPerfil()) ? null : datos.getCursoId());
        return usuarioRepository.save(actual);
    }

    // 8. Regla: un docente con asignaciones no se puede borrar
    public boolean tieneAsignaciones(Long id) {
        return asignacionRepository.existsByDocenteId(id);
    }

    // 9. Borrar
    public boolean borrar(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 10. Login: devuelve los datos del usuario SIN la contraseña, o null si no coincide
    public LoginResponse login(LoginRequest request) {
        if (request.getCorreo() == null || request.getPassword() == null) {
            return null;
        }
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(request.getCorreo().trim());
        if (usuario == null || !usuario.getPassword().equals(request.getPassword())) {
            return null;
        }
        return new LoginResponse(usuario.getId(), usuario.getNombre(), usuario.getCorreo(),
                usuario.getPerfil(), usuario.getCursoId());
    }
}
