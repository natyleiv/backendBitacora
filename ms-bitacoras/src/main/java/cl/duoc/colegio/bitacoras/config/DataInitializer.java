package cl.duoc.colegio.bitacoras.config;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import cl.duoc.colegio.bitacoras.model.EstadoBitacora;
import cl.duoc.colegio.bitacoras.model.PreguntaOrientadora;
import cl.duoc.colegio.bitacoras.repository.BitacoraRepository;
import cl.duoc.colegio.bitacoras.repository.PreguntaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosDemo(BitacoraRepository bitacoraRepository,
                                      PreguntaRepository preguntaRepository) {
        return args -> {
            if (bitacoraRepository.count() == 0) {
                // 1) Bitácoras (save devuelve la bitácora ya con su id)
                Bitacora matematica = bitacoraRepository.save(new Bitacora(null, 1L,
                        "Bitácora de Octubre - Matemática",
                        "Registrar lo aprendido en cada clase de Matemática durante octubre.",
                        "Después de cada clase escribe qué trabajaste, qué hiciste y qué aprendiste.",
                        10, 2026, EstadoBitacora.PUBLICADA, "01/10/2026", null));

                Bitacora historia = bitacoraRepository.save(new Bitacora(null, 3L,
                        "Bitácora de Octubre - Historia",
                        "Registrar los aprendizajes de Historia del mes.",
                        "Escribe un registro por clase y agrega una foto de tu cuaderno si quieres.",
                        10, 2026, EstadoBitacora.PUBLICADA, "01/10/2026", null));

                // 2) Preguntas de cada bitácora (las mismas que tiene la app)
                preguntaRepository.saveAll(List.of(
                        new PreguntaOrientadora(null, matematica.getId(), 1, "¿Qué contenido trabajamos hoy?"),
                        new PreguntaOrientadora(null, matematica.getId(), 2, "¿Qué fue lo más difícil?"),
                        new PreguntaOrientadora(null, matematica.getId(), 3, "¿Qué te gustaría repasar?"),
                        new PreguntaOrientadora(null, historia.getId(), 1, "¿Qué hecho histórico vimos?"),
                        new PreguntaOrientadora(null, historia.getId(), 2, "¿Por qué es importante?")
                ));
            }
        };
    }
}