package cl.duoc.colegio.usuarios.controller;

import cl.duoc.colegio.usuarios.model.Asignacion;
import cl.duoc.colegio.usuarios.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignaciones")
public class AsignacionController {

    @Autowired
    private AsignacionService asignacionService;

    // 1. GET: listar todas
    @GetMapping
    public ResponseEntity<List<Asignacion>> listar() {
        return ResponseEntity.ok(asignacionService.findAll());
    }

    // 2. GET: buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Asignacion> buscarPorId(@PathVariable Long id) {
        Asignacion asignacion = asignacionService.buscarPorId(id);
        if (asignacion == null) return ResponseEntity.notFound().build();             // 404
        return ResponseEntity.ok(asignacion);
    }

    // 3. GET: asignaciones de un docente (la app luego llama a ms-bitacoras)
    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<Asignacion>> listarPorDocente(@PathVariable Long docenteId) {
        return ResponseEntity.ok(asignacionService.buscarPorDocente(docenteId));
    }

    // 4. GET: asignaciones de un curso (para que el estudiante vea las bitácoras de su curso)
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Asignacion>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(asignacionService.buscarPorCurso(cursoId));
    }

    // 5. POST: crear
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Asignacion asignacion) {
        String error = asignacionService.validar(asignacion);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);                          // 400
        }
        if (asignacionService.existeDuplicada(asignacion)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)                         // 409
                    .body("Esa asignación ya existe (mismo docente, curso y asignatura)");
        }
        return new ResponseEntity<>(asignacionService.registrar(asignacion), HttpStatus.CREATED); // 201
    }

    // 6. DELETE: borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        if (!asignacionService.borrar(id)) return ResponseEntity.notFound().build();  // 404
        return ResponseEntity.noContent().build();                                     // 204
    }
}
