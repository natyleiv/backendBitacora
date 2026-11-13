package cl.duoc.colegio.bitacoras.service;

import cl.duoc.colegio.bitacoras.model.Bitacora;
import cl.duoc.colegio.bitacoras.model.EstadoEnvio;
import cl.duoc.colegio.bitacoras.model.Registro;
import cl.duoc.colegio.bitacoras.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

@Service
public class RegistroService {
    @Autowired
    private RegistroRepository registroRepository;

    // "uuuu" + STRICT: rechaza fechas que no existen, como 31/02/2026
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final String[] MESES = {"enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};

    // 1. Buscar por ID
    public Registro buscarPorId(Long id) {
        return registroRepository.findById(id).orElse(null);
    }

    // 2. Aportes ENVIADOS de una bitácora (lo que ve el docente)
    public List<Registro> listarEnviadosDeBitacora(Long bitacoraId) {
        return registroRepository.findByBitacoraIdAndEstadoEnvioOrderByFechaCreacionDesc(
                bitacoraId, EstadoEnvio.ENVIADO);
    }

    // 3. Historial de un estudiante (borradores y enviados)
    public List<Registro> listarPorEstudiante(Long estudianteId) {
        return registroRepository.findByEstudianteIdOrderByFechaCreacionDesc(estudianteId);
    }

    // 4. ¿El estudiante quiere enviarlo o solo guardarlo como borrador?
    public boolean quiereEnviar(Registro registro) {
        return EstadoEnvio.ENVIADO.equals(registro.getEstadoEnvio());
    }

    // 5. Validar. Devuelve el mensaje de error, o null si está todo bien.
    //    Son las mismas reglas que valida la app.
    public String validar(Registro registro, Bitacora bitacora) {
        if (registro.getEstudianteId() == null) {
            return "Falta el estudiante";
        }
        if (!fechaValida(registro.getFechaClase(), bitacora)) {
            return "Elija un día de " + MESES[bitacora.getMes() - 1] + " " + bitacora.getAnio()
                    + ", hasta la fecha de hoy";
        }
        if (vacio(registro.getContenido())) {
            return "El contenido trabajado es obligatorio";
        }
        // Actividad y aprendizaje solo son obligatorios al ENVIAR (un borrador puede ir incompleto)
        if (quiereEnviar(registro)) {
            if (vacio(registro.getActividad())) {
                return "Describe la actividad que realizaste";
            }
            if (vacio(registro.getAprendizaje())) {
                return "Cuéntanos qué aprendiste";
            }
        }
        // El enlace es opcional, pero si viene debe partir con http:// o https://
        if (!vacio(registro.getEnlace())) {
            String enlace = registro.getEnlace().trim();
            if (!enlace.startsWith("http://") && !enlace.startsWith("https://")) {
                return "El enlace debe comenzar con http:// o https://";
            }
        }
        return null;
    }

    // La fecha debe existir, no ser futura y estar dentro del mes de la bitácora
    private boolean fechaValida(String texto, Bitacora bitacora) {
        if (vacio(texto)) return false;
        try {
            LocalDate fecha = LocalDate.parse(texto.trim(), FORMATO);
            return !fecha.isAfter(LocalDate.now())
                    && fecha.getMonthValue() == bitacora.getMes()
                    && fecha.getYear() == bitacora.getAnio();
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean vacio(String texto) {
        return texto == null || texto.isBlank();
    }

    // 6. Registrar uno nuevo (como BORRADOR o ENVIADO)
    public Registro registrar(Registro registro) {
        long ahora = System.currentTimeMillis();
        registro.setId(null);
        registro.setFechaCreacion(ahora);
        if (quiereEnviar(registro)) {
            registro.setEstadoEnvio(EstadoEnvio.ENVIADO);
            registro.setFechaEnvio(ahora);
        } else {
            registro.setEstadoEnvio(EstadoEnvio.BORRADOR);
            registro.setFechaEnvio(null);
        }
        return registroRepository.save(registro);
    }

    // 7. Actualizar un borrador (el controller revisa antes que siga en BORRADOR).
    //    No se cambian la bitácora ni el estudiante.
    public Registro actualizar(Registro actual, Registro datos) {
        actual.setFechaClase(datos.getFechaClase());
        actual.setContenido(datos.getContenido());
        actual.setActividad(datos.getActividad());
        actual.setAprendizaje(datos.getAprendizaje());
        actual.setDudas(datos.getDudas());
        actual.setFotoUri(datos.getFotoUri());
        actual.setEnlace(datos.getEnlace());
        if (quiereEnviar(datos)) {
            actual.setEstadoEnvio(EstadoEnvio.ENVIADO);
            actual.setFechaEnvio(System.currentTimeMillis());
        }
        return registroRepository.save(actual);
    }

    // 8. Borrar
    public boolean borrar(Long id) {
        if (registroRepository.existsById(id)) {
            registroRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
