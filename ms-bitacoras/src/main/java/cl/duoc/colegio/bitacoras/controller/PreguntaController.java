package cl.duoc.colegio.bitacoras.controller;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import cl.duoc.colegio.bitacoras.model.EstadoBitacora;
import cl.duoc.colegio.bitacoras.model.PreguntaOrientadora;
import cl.duoc.colegio.bitacoras.service.BitacoraService;
import cl.duoc.colegio.bitacoras.service.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bitacoras/{bitacoraId}/preguntas")
public class PreguntaController {

    @Autowired
    private PreguntaService preguntaService;

    @Autowired
    private BitacoraService bitacoraService;

    // 1. GET: preguntas de una bitácora
    @GetMapping
    public ResponseEntity<List<PreguntaOrientadora>> listar(@PathVariable Long bitacoraId) {
        if (bitacoraService.buscarPorId(bitacoraId) == null) {
            return ResponseEntity.notFound().build();                                  // 404
        }
        return ResponseEntity.ok(preguntaService.listarPorBitacora(bitacoraId));
    }

    // 2. PUT: reemplazar todas las preguntas (solo si la bitácora está en BORRADOR)
    @PutMapping
    public ResponseEntity<?> reemplazar(@PathVariable Long bitacoraId,
                                        @RequestBody List<PreguntaOrientadora> preguntas) {
        Bitacora bitacora = bitacoraService.buscarPorId(bitacoraId);
        if (bitacora == null) return ResponseEntity.notFound().build();
        if (!EstadoBitacora.BORRADOR.equals(bitacora.getEstado())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)                          // 409
                    .body("Esta bitácora ya no se puede editar.");
        }
        String error = preguntaService.validar(preguntas);
        if (error != null) return ResponseEntity.badRequest().body(error);             // 400
        return ResponseEntity.ok(preguntaService.reemplazar(bitacoraId, preguntas));
    }
}