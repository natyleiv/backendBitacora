package cl.duoc.colegio.bitacoras.controller;

import cl.duoc.colegio.bitacoras.model.EstadoEnvio;
import cl.duoc.colegio.bitacoras.model.Registro;
import cl.duoc.colegio.bitacoras.model.Retroalimentacion;
import cl.duoc.colegio.bitacoras.service.RegistroService;
import cl.duoc.colegio.bitacoras.service.RetroalimentacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registros/{registroId}/retroalimentacion")
public class RetroalimentacionController {

    @Autowired
    private RetroalimentacionService retroalimentacionService;

    @Autowired
    private RegistroService registroService;

    // 1. GET: comentario del docente sobre un registro
    @GetMapping
    public ResponseEntity<Retroalimentacion> obtener(@PathVariable Long registroId) {
        if (registroService.buscarPorId(registroId) == null) {
            return ResponseEntity.notFound().build();                     // 404: el registro no existe
        }
        Retroalimentacion retro = retroalimentacionService.buscarPorRegistro(registroId);
        if (retro == null) {
            return ResponseEntity.noContent().build();                    // 204: todavía no lo revisan
        }
        return ResponseEntity.ok(retro);                                  // 200: ya está revisado
    }

    // 2. PUT: guardar o actualizar el comentario (solo en registros ENVIADOS)
    @PutMapping
    public ResponseEntity<?> guardar(@PathVariable Long registroId,
                                     @RequestBody Retroalimentacion datos) {
        Registro registro = registroService.buscarPorId(registroId);
        if (registro == null) return ResponseEntity.notFound().build();
        if (!EstadoEnvio.ENVIADO.equals(registro.getEstadoEnvio())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)             // 409
                    .body("Solo se puede comentar un registro enviado.");
        }
        String error = retroalimentacionService.validar(datos);
        if (error != null) return ResponseEntity.badRequest().body(error); // 400
        return ResponseEntity.ok(retroalimentacionService.guardar(registroId, datos));
    }
}
