package cl.duoc.colegio.bitacoras.controller;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import cl.duoc.colegio.bitacoras.model.EstadoBitacora;
import cl.duoc.colegio.bitacoras.service.BitacoraService;
import cl.duoc.colegio.bitacoras.service.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bitacoras")
public class BitacoraController {

    @Autowired
    private BitacoraService bitacoraService;

    @Autowired
    private PreguntaService preguntaService;

    // 1. GET: listar todas
    @GetMapping
    public ResponseEntity<List<Bitacora>> listar() {
        return ResponseEntity.ok(bitacoraService.findAll());
    }

    // 2. GET: buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Bitacora> buscarPorId(@PathVariable Long id) {
        Bitacora bitacora = bitacoraService.buscarPorId(id);
        if (bitacora == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bitacora);
    }

    // 3. GET: bitácoras de una asignación
    @GetMapping("/asignacion/{asignacionId}")
    public ResponseEntity<List<Bitacora>> listarPorAsignacion(@PathVariable Long asignacionId) {
        return ResponseEntity.ok(bitacoraService.buscarPorAsignacion(asignacionId));
    }

    // 4. POST: crear (queda en BORRADOR)
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Bitacora bitacora) {
        String error = bitacoraService.validar(bitacora);
        if (error != null) {
            return ResponseEntity.badRequest().body(error);                          // 400
        }
        if (bitacoraService.existeDuplicada(bitacora, null)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)                         // 409
                    .body("Ya existe una bitácora de este mes para el curso y asignatura elegidos");
        }
        return new ResponseEntity<>(bitacoraService.registrar(bitacora), HttpStatus.CREATED); // 201
    }

    // 5. PUT: editar (solo en BORRADOR)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Bitacora datos) {
        Bitacora actual = bitacoraService.buscarPorId(id);
        if (actual == null) return ResponseEntity.notFound().build();                 // 404
        if (!EstadoBitacora.BORRADOR.equals(actual.getEstado())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Esta bitácora ya no se puede editar.");
        }
        String error = bitacoraService.validar(datos);
        if (error != null) return ResponseEntity.badRequest().body(error);
        if (bitacoraService.existeDuplicada(datos, id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe una bitácora de este mes para el curso y asignatura elegidos");
        }
        return ResponseEntity.ok(bitacoraService.actualizar(actual, datos));
    }

    // 6. PATCH: publicar (solo desde BORRADOR)
    @PatchMapping("/{id}/publicar")
    public ResponseEntity<?> publicar(@PathVariable Long id) {
        Bitacora bitacora = bitacoraService.buscarPorId(id);
        if (bitacora == null) return ResponseEntity.notFound().build();
        if (!EstadoBitacora.BORRADOR.equals(bitacora.getEstado())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Solo se puede publicar una bitácora en borrador.");
        }
        return ResponseEntity.ok(bitacoraService.publicar(bitacora));
    }

    // 7. PATCH: cerrar (solo desde PUBLICADA)
    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrar(@PathVariable Long id) {
        Bitacora bitacora = bitacoraService.buscarPorId(id);
        if (bitacora == null) return ResponseEntity.notFound().build();
        if (!EstadoBitacora.PUBLICADA.equals(bitacora.getEstado())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Solo se puede cerrar una bitácora publicada.");
        }
        return ResponseEntity.ok(bitacoraService.cerrar(bitacora));
    }

    // 8. DELETE: borrar (solo en BORRADOR, para no perder aportes de estudiantes)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        Bitacora bitacora = bitacoraService.buscarPorId(id);
        if (bitacora == null) return ResponseEntity.notFound().build();
        if (!EstadoBitacora.BORRADOR.equals(bitacora.getEstado())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Solo se puede eliminar una bitácora en borrador.");
        }
        preguntaService.borrarPorBitacora(id);   // primero sus preguntas
        bitacoraService.borrar(id);              // después la bitácora
        return ResponseEntity.noContent().build();                                     // 204
    }
}