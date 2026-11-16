package cl.duoc.colegio.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lo que devuelve el login: los datos del usuario SIN la contraseña
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String nombre;
    private String correo;
    private String perfil;
    private Long cursoId;
}
