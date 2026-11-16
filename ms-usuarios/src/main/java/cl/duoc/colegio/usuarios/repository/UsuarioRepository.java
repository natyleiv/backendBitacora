package cl.duoc.colegio.usuarios.repository;

import cl.duoc.colegio.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Para el login
    Usuario findByCorreoIgnoreCase(String correo);

    // Estudiantes de un curso
    List<Usuario> findByCursoId(Long cursoId);

    // Regla: el correo no se puede repetir
    boolean existsByCorreoIgnoreCase(String correo);

    // Lo mismo, pero sin compararse consigo mismo (al editar)
    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Long id);

    // Para no borrar un curso que todavía tiene estudiantes
    boolean existsByCursoId(Long cursoId);
}
