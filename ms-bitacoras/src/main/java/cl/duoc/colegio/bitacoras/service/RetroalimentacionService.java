package cl.duoc.colegio.bitacoras.service;

import cl.duoc.colegio.bitacoras.model.Retroalimentacion;
import cl.duoc.colegio.bitacoras.repository.RetroalimentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetroalimentacionService {
    @Autowired
    private RetroalimentacionRepository retroalimentacionRepository;

    // 1. Comentario de un registro, o null si todavía no lo revisan
    public Retroalimentacion buscarPorRegistro(Long registroId) {
        return retroalimentacionRepository.findByRegistroId(registroId).orElse(null);
    }

    // 2. Validar. Devuelve el mensaje de error, o null si está todo bien.
    //    Es la misma regla de la app: al menos 5 caracteres.
    public String validar(Retroalimentacion datos) {
        if (datos.getDocenteId() == null) {
            return "Falta el docente";
        }
        if (datos.getComentario() == null || datos.getComentario().trim().length() < 5) {
            return "Escriba un comentario de al menos 5 caracteres";
        }
        return null;
    }

    // 3. Guardar: si el registro no tenía comentario lo crea; si ya tenía, lo actualiza.
    //    Así se respeta "un solo comentario por registro".
    public Retroalimentacion guardar(Long registroId, Retroalimentacion datos) {
        Retroalimentacion retro = buscarPorRegistro(registroId);
        if (retro == null) {
            retro = new Retroalimentacion();
            retro.setRegistroId(registroId);
        }
        retro.setDocenteId(datos.getDocenteId());
        retro.setComentario(datos.getComentario().trim());
        retro.setFecha(System.currentTimeMillis());
        return retroalimentacionRepository.save(retro);
    }
}
