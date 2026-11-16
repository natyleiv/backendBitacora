package cl.duoc.colegio.usuarios.controller;

import cl.duoc.colegio.usuarios.model.Usuario;
import cl.duoc.colegio.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. GET: listar todos (la contraseña nunca se devuelve)
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    // 2. GET: buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return ResponseEntity.notFound().build();                // 404
        return ResponseEntity.ok(usuario);
    }

    // 3. GET: estudiantes de un curso
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Usuario>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(usuarioService.buscarPorCurso(cursoId));
    }

    // 4. POST: crear
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        String error = usuarioService.validar(usuario);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);                          // 400
        }
        if (usuarioService.existeCorreo(usuario.getCorreo(), null)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)                         // 409
                    .body("Ya existe un usuario con ese correo");
        }
        return new ResponseEntity<>(usuarioService.registrar(usuario), HttpStatus.CREATED); // 201
    }

    // 5. PUT: editar
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        Usuario actual = usuarioService.buscarPorId(id);
        if (actual == null) return ResponseEntity.notFound().build();                 // 404
        String error = usuarioService.validar(datos);
        if (error != null) return ResponseEntity.badRequest().body(error);            // 400
        if (usuarioService.existeCorreo(datos.getCorreo(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con ese correo");                      // 409
        }
        return ResponseEntity.ok(usuarioService.actualizar(actual, datos));
    }

    // 6. DELETE: borrar (un docente con asignaciones no se puede borrar)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        if (usuarioService.buscarPorId(id) == null) return ResponseEntity.notFound().build();
        if (usuarioService.tieneAsignaciones(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar: el docente tiene asignaciones");
        }
        usuarioService.borrar(id);
        return ResponseEntity.noContent().build();                                     // 204
    }
}
