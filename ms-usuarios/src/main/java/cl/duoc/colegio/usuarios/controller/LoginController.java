package cl.duoc.colegio.usuarios.controller;

import cl.duoc.colegio.usuarios.dto.LoginRequest;
import cl.duoc.colegio.usuarios.dto.LoginResponse;
import cl.duoc.colegio.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    // POST: iniciar sesión. Devuelve el usuario SIN la contraseña.
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getCorreo() == null || request.getCorreo().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Ingrese correo y contraseña");   // 400
        }
        LoginResponse respuesta = usuarioService.login(request);
        if (respuesta == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)                     // 401
                    .body("Correo o contraseña incorrectos");
        }
        return ResponseEntity.ok(respuesta);                                           // 200
    }
}
