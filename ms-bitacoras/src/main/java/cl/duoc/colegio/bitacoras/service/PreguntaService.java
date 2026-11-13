package cl.duoc.colegio.bitacoras.service;

import cl.duoc.colegio.bitacoras.model.PreguntaOrientadora;
import cl.duoc.colegio.bitacoras.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PreguntaService {
    @Autowired
    private PreguntaRepository preguntaRepository;

    // 1. Listar las preguntas de una bitácora, en orden
    public List<PreguntaOrientadora> listarPorBitacora(Long bitacoraId) {
        return preguntaRepository.findByBitacoraIdOrderByOrdenAsc(bitacoraId);
    }

    // 2. Validar. Devuelve el mensaje de error, o null si está todo bien.
    public String validar(List<PreguntaOrientadora> preguntas) {
        if (preguntas == null || preguntas.isEmpty()) {
            return "Escriba al menos una pregunta";
        }
        for (PreguntaOrientadora pregunta : preguntas) {
            if (pregunta.getTexto() == null || pregunta.getTexto().isBlank()) {
                return "Ninguna pregunta puede estar vacía";
            }
        }
        return null;
    }

    // 3. Reemplazar todas las preguntas de una bitácora por las nuevas.
    //    Borra las anteriores y guarda las nuevas numeradas 1, 2, 3...
    @Transactional
    public List<PreguntaOrientadora> reemplazar(Long bitacoraId, List<PreguntaOrientadora> preguntas) {
        preguntaRepository.deleteByBitacoraId(bitacoraId);
        for (int i = 0; i < preguntas.size(); i++) {
            PreguntaOrientadora pregunta = preguntas.get(i);
            pregunta.setId(null);
            pregunta.setBitacoraId(bitacoraId);
            pregunta.setOrden(i + 1);
            pregunta.setTexto(pregunta.getTexto().trim());
        }
        return preguntaRepository.saveAll(preguntas);
    }

    // 4. Borrar todas las preguntas de una bitácora (cuando se borra la bitácora)
    @Transactional
    public void borrarPorBitacora(Long bitacoraId) {
        preguntaRepository.deleteByBitacoraId(bitacoraId);
    }
}
