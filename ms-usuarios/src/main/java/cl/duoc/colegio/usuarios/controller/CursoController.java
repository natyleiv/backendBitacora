package cl.duoc.colegio.usuarios.controller;

import cl.duoc.colegio.usuarios.model.Curso;
import cl.duoc.colegio.usuarios.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService service;

    // 1. GET: listar todos
    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    // 2. GET: buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        Curso obj = service.buscarPorId(id);
        if (obj == null) return ResponseEntity.notFound().build();                    // 404
        return ResponseEntity.ok(obj);
    }

    // 3. POST: crear
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Curso obj) {
        String error = service.validar(obj);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);                          // 400
        }
        if (service.existeNombre(obj.getNombre(), null)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)                         // 409
                    .body("Ya existe un curso con ese nombre");
        }
        return new ResponseEntity<>(service.registrar(obj), HttpStatus.CREATED);     // 201
    }

    // 4. PUT: editar
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Curso datos) {
        Curso actual = service.buscarPorId(id);
        if (actual == null) return ResponseEntity.notFound().build();                 // 404
        String error = service.validar(datos);
        if (error != null) return ResponseEntity.badRequest().body(error);            // 400
        if (service.existeNombre(datos.getNombre(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un curso con ese nombre");             // 409
        }
        return ResponseEntity.ok(service.actualizar(actual, datos));
    }

    // 5. DELETE: borrar (no si está en uso)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        if (service.buscarPorId(id) == null) return ResponseEntity.notFound().build(); // 404
        if (service.estaEnUso(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)                          // 409
                    .body("No se puede eliminar: el curso tiene estudiantes o asignaciones");
        }
        service.borrar(id);
        return ResponseEntity.noContent().build();                                     // 204
    }
}
