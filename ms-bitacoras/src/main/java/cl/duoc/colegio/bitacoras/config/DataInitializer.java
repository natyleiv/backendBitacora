package cl.duoc.colegio.bitacoras.config;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import cl.duoc.colegio.bitacoras.model.EstadoBitacora;
import cl.duoc.colegio.bitacoras.repository.BitacoraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatosDemo(BitacoraRepository bitacoraRepository) {
        return args -> {
            if (bitacoraRepository.count() == 0) {
                Bitacora matematica = new Bitacora(null, 1L,
                        "Bitácora de Octubre - Matemática",
                        "Registrar lo aprendido en cada clase de Matemática durante octubre.",
                        "Después de cada clase escribe qué trabajaste, qué hiciste y qué aprendiste.",
                        10, 2026, EstadoBitacora.PUBLICADA, "01/10/2026", null);

                Bitacora historia = new Bitacora(null, 3L,
                        "Bitácora de Octubre - Historia",
                        "Registrar los aprendizajes de Historia del mes.",
                        "Escribe un registro por clase y agrega una foto de tu cuaderno si quieres.",
                        10, 2026, EstadoBitacora.PUBLICADA, "01/10/2026", null);

                bitacoraRepository.saveAll(List.of(matematica, historia));
            }
        };
    }
}
