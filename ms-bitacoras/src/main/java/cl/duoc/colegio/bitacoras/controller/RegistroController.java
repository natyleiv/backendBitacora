package cl.duoc.colegio.bitacoras.controller;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import cl.duoc.colegio.bitacoras.model.EstadoBitacora;
import cl.duoc.colegio.bitacoras.model.EstadoEnvio;
import cl.duoc.colegio.bitacoras.model.Registro;
import cl.duoc.colegio.bitacoras.service.BitacoraService;
import cl.duoc.colegio.bitacoras.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registros")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @Autowired
    private BitacoraService bitacoraService;

    // 1. GET: buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Registro> buscarPorId(@PathVariable Long id) {
        Registro registro = registroService.buscarPorId(id);
        if (registro == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(registro);
    }

    // 2. GET: aportes ENVIADOS de una bitácora (vista del docente)
    @GetMapping("/bitacora/{bitacoraId}")
    public ResponseEntity<List<Registro>> listarPorBitacora(@PathVariable Long bitacoraId) {
        if (bitacoraService.buscarPorId(bitacoraId) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(registroService.listarEnviadosDeBitacora(bitacoraId));
    }

    // 3. GET: historial de un estudiante
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<Registro>> listarPorEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(registroService.listarPorEstudiante(estudianteId));
    }

    // 4. POST: crear (como BORRADOR o ENVIADO). Solo en bitácoras PUBLICADAS.
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Registro registro) {
        if (registro.getBitacoraId() == null) {
            return ResponseEntity.badRequest().body("Falta la bitácora");                 // 400
        }
        Bitacora bitacora = bitacoraService.buscarPorId(registro.getBitacoraId());
        if (bitacora == null) return ResponseEntity.notFound().build();                  // 404
        if (!EstadoBitacora.PUBLICADA.equals(bitacora.getEstado())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)                            // 409
                    .body("La bitácora ya no está activa. No se guardó el registro.");
        }
        String error = registroService.validar(registro, bitacora);
        if (error != null) return ResponseEntity.badRequest().body(error);
        return new ResponseEntity<>(registroService.registrar(registro), HttpStatus.CREATED); // 201
    }

    // 5. PUT: editar un BORRADOR (y opcionalmente enviarlo)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Registro datos) {
        Registro actual = registroService.buscarPorId(id);
        if (actual == null) return ResponseEntity.notFound().build();
        if (!EstadoEnvio.BORRADOR.equals(actual.getEstadoEnvio())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Este registro ya fue enviado y no se puede editar.");
        }
        Bitacora bitacora = bitacoraService.buscarPorId(actual.getBitacoraId());
        if (bitacora == null || !EstadoBitacora.PUBLICADA.equals(bitacora.getEstado())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La bitácora ya no está activa. No se guardó el registro.");
        }
        // El estudiante no cambia: se valida con el del registro original
        datos.setEstudianteId(actual.getEstudianteId());
        String error = registroService.validar(datos, bitacora);
        if (error != null) return ResponseEntity.badRequest().body(error);
        return ResponseEntity.ok(registroService.actualizar(actual, datos));
    }

    // 6. DELETE: borrar (solo BORRADOR; lo enviado queda para el docente)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        Registro registro = registroService.buscarPorId(id);
        if (registro == null) return ResponseEntity.notFound().build();
        if (!EstadoEnvio.BORRADOR.equals(registro.getEstadoEnvio())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Solo se puede eliminar un registro en borrador.");
        }
        registroService.borrar(id);
        return ResponseEntity.noContent().build();                                        // 204
    }
}
