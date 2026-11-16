package cl.duoc.colegio.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lo que envía la app para iniciar sesión
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String correo;
    private String password;
}
