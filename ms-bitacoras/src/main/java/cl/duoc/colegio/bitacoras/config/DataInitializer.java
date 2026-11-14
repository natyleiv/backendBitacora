package cl.duoc.colegio.bitacoras.config;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import cl.duoc.colegio.bitacoras.model.EstadoBitacora;
import cl.duoc.colegio.bitacoras.model.EstadoEnvio;
import cl.duoc.colegio.bitacoras.model.PreguntaOrientadora;
import cl.duoc.colegio.bitacoras.model.Registro;
import cl.duoc.colegio.bitacoras.model.Retroalimentacion;
import cl.duoc.colegio.bitacoras.repository.BitacoraRepository;
import cl.duoc.colegio.bitacoras.repository.PreguntaRepository;
import cl.duoc.colegio.bitacoras.repository.RegistroRepository;
import cl.duoc.colegio.bitacoras.repository.RetroalimentacionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosDemo(BitacoraRepository bitacoraRepository,
                                      PreguntaRepository preguntaRepository,
                                      RegistroRepository registroRepository,
                                      RetroalimentacionRepository retroalimentacionRepository) {
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

                // 3) Registros de ejemplo
                long ahora = System.currentTimeMillis();

                // ENVIADO: Estudiante Uno (id 3). El docente lo ve.
                Registro enviado = registroRepository.save(new Registro(null,
                        matematica.getId(), 3L, "05/10/2026",
                        "Fracciones equivalentes",
                        "Resolvimos ejercicios de fracciones en parejas",
                        "Aprendí a encontrar fracciones equivalentes multiplicando arriba y abajo",
                        "No sé bien cómo simplificar fracciones grandes",
                        null, null,
                        EstadoEnvio.ENVIADO, ahora, ahora));

                // BORRADOR: Estudiante Dos (id 4). El docente NO lo ve.
                registroRepository.save(new Registro(null,
                        matematica.getId(), 4L, "06/10/2026",
                        "Comparación de fracciones",
                        null, null, null,
                        null, null,
                        EstadoEnvio.BORRADOR, ahora, null));

                // 4) Retroalimentación del docente 1 sobre el registro enviado
                retroalimentacionRepository.save(new Retroalimentacion(null,
                        enviado.getId(), 1L,
                        "Muy buen trabajo, revisa cómo simplificar antes de comparar.",
                        ahora));
            }
        };
    }
}