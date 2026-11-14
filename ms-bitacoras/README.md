# ms-bitacoras

Microservicio de bitácoras, preguntas orientadoras, registros de clase y retroalimentación de **Mi Bitácora**. Puerto **8082**, base de datos `colegio_bitacoras`.

**Responsable:** Juan Pablo Marambio Zagal

Los ids de `ms-usuarios` (`asignacionId`, `estudianteId`, `docenteId`) se guardan como números, sin clave foránea, porque viven en otra base de datos.

## Estructura

| Paquete | Contenido |
|---|---|
| `model` | Entidades `Bitacora`, `PreguntaOrientadora`, `Registro`, `Retroalimentacion` y las constantes de estado `EstadoBitacora` y `EstadoEnvio` |
| `repository` | Un `JpaRepository` por entidad, con consultas derivadas del nombre del método |
| `service` | Validaciones y reglas de negocio |
| `controller` | Endpoints REST |
| `config` | `DataInitializer`: carga los datos de prueba al iniciar si la base está vacía |

## Entidades y validaciones

| Entidad | Campos | Validaciones |
|---|---|---|
| `Bitacora` | `id`, `asignacionId`, `titulo`, `proposito`, `instrucciones`, `mes`, `anio`, `estado` (`BORRADOR` / `PUBLICADA` / `CERRADA`), `fechaPublicacion`, `fechaCierre` | `asignacionId` obligatorio; `titulo` de al menos 5 caracteres; `proposito` e `instrucciones` obligatorios; `mes` entre 1 y 12; `anio` entre 2025 y 2100 |
| `PreguntaOrientadora` | `id`, `bitacoraId`, `orden`, `texto` | Al menos una por bitácora; ninguna vacía |
| `Registro` | `id`, `bitacoraId`, `estudianteId`, `fechaClase`, `contenido`, `actividad`, `aprendizaje`, `dudas`, `fotoUri`, `enlace`, `estadoEnvio` (`BORRADOR` / `ENVIADO`), `fechaCreacion`, `fechaEnvio` | `fechaClase` válida (dd/MM/yyyy), no futura y dentro del mes de la bitácora; `contenido` obligatorio; `actividad` y `aprendizaje` obligatorios al enviar; `enlace` opcional, con `http://` o `https://` |
| `Retroalimentacion` | `id`, `registroId` (único), `docenteId`, `comentario`, `fecha` | `docenteId` obligatorio; `comentario` de al menos 5 caracteres |

## Reglas de negocio

- No puede haber dos bitácoras con el mismo `asignacionId`, `mes` y `anio`.
- Una bitácora nueva siempre parte en `BORRADOR`. Solo se edita o elimina en `BORRADOR`, se publica desde `BORRADOR` y se cierra desde `PUBLICADA`.
- Al eliminar una bitácora se eliminan también sus preguntas.
- Solo se puede registrar en una bitácora `PUBLICADA`.
- Un registro se puede guardar como `BORRADOR` (incompleto) o `ENVIADO`. Solo se edita o elimina mientras está en `BORRADOR`.
- El docente ve solo los registros `ENVIADO`.
- Cada registro tiene como máximo un comentario del docente, y solo se puede comentar un registro `ENVIADO`.
- Un registro está "revisado" cuando tiene retroalimentación (no se guarda como columna aparte).

## Endpoints

### Bitácoras

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| `GET` | `/bitacoras` | Lista todas las bitácoras | 200 |
| `GET` | `/bitacoras/{id}` | Busca una bitácora | 200, 404 |
| `GET` | `/bitacoras/asignacion/{asignacionId}` | Bitácoras de una asignación (vista del docente) | 200 |
| `POST` | `/bitacoras` | Crea una bitácora en `BORRADOR` | 201, 400, 409 (duplicada) |
| `PUT` | `/bitacoras/{id}` | Edita (solo en `BORRADOR`) | 200, 400, 404, 409 |
| `PATCH` | `/bitacoras/{id}/publicar` | `BORRADOR` → `PUBLICADA` | 200, 404, 409 |
| `PATCH` | `/bitacoras/{id}/cerrar` | `PUBLICADA` → `CERRADA` | 200, 404, 409 |
| `DELETE` | `/bitacoras/{id}` | Elimina (solo en `BORRADOR`) junto con sus preguntas | 204, 404, 409 |

### Preguntas orientadoras

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| `GET` | `/bitacoras/{bitacoraId}/preguntas` | Preguntas de una bitácora, en orden | 200, 404 |
| `PUT` | `/bitacoras/{bitacoraId}/preguntas` | Reemplaza todas las preguntas (solo en `BORRADOR`) | 200, 400, 404, 409 |

### Registros

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| `GET` | `/registros/{id}` | Busca un registro | 200, 404 |
| `GET` | `/registros/bitacora/{bitacoraId}` | Registros **enviados** de una bitácora (vista del docente) | 200, 404 |
| `GET` | `/registros/estudiante/{estudianteId}` | Historial del estudiante (borradores y enviados) | 200 |
| `POST` | `/registros` | Crea un registro como `BORRADOR` o `ENVIADO` (solo en bitácoras `PUBLICADA`) | 201, 400, 404, 409 |
| `PUT` | `/registros/{id}` | Edita un borrador; con `"estadoEnvio": "ENVIADO"` además lo envía | 200, 400, 404, 409 |
| `DELETE` | `/registros/{id}` | Elimina (solo en `BORRADOR`) | 204, 404, 409 |

### Retroalimentación

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| `GET` | `/registros/{registroId}/retroalimentacion` | Comentario del docente sobre un registro | 200, 204 (aún sin revisar), 404 |
| `PUT` | `/registros/{registroId}/retroalimentacion` | Crea o actualiza el comentario (solo en registros `ENVIADO`) | 200, 400, 404, 409 |

**Códigos:** 400 = datos inválidos (el cuerpo trae el mensaje), 404 = no existe, 409 = se rompe una regla de negocio.

## Ejemplos de cuerpo (JSON)

Crear bitácora (`POST /bitacoras`):

```json
{
  "asignacionId": 2,
  "titulo": "Bitácora de Noviembre - Lenguaje",
  "proposito": "Registrar lo aprendido en Lenguaje durante noviembre.",
  "instrucciones": "Escribe un registro después de cada clase.",
  "mes": 11,
  "anio": 2026
}
```

Reemplazar preguntas (`PUT /bitacoras/{id}/preguntas`):

```json
[
  { "texto": "¿Qué leímos hoy?" },
  { "texto": "¿Qué palabra nueva aprendiste?" }
]
```

Crear registro (`POST /registros`):

```json
{
  "bitacoraId": 1,
  "estudianteId": 3,
  "fechaClase": "05/10/2026",
  "contenido": "Fracciones equivalentes",
  "actividad": "Ejercicios en parejas",
  "aprendizaje": "Cómo encontrar fracciones equivalentes",
  "estadoEnvio": "ENVIADO"
}
```

Comentar un registro (`PUT /registros/{id}/retroalimentacion`):

```json
{
  "docenteId": 1,
  "comentario": "Muy buen trabajo, revisa cómo simplificar antes de comparar."
}
```

## Datos de prueba

Si la tabla `bitacoras` está vacía, al iniciar se cargan:

| Dato | Detalle |
|---|---|
| Bitácora 1 | Matemática, octubre 2026, asignación 1, `PUBLICADA`, con 3 preguntas |
| Bitácora 2 | Historia, octubre 2026, asignación 3, `PUBLICADA`, con 2 preguntas |
| Registro 1 | Estudiante 3 en Matemática, 05/10/2026, `ENVIADO` y con retroalimentación del docente 1 |
| Registro 2 | Estudiante 4 en Matemática, 06/10/2026, `BORRADOR` |

Para volver a cargarlos: detener el microservicio, vaciar las tablas (`TRUNCATE`) de `colegio_bitacoras` y ejecutarlo de nuevo.

## Tareas

- [x] Constantes de estado `EstadoBitacora` y `EstadoEnvio`
- [x] Entidades `Bitacora`, `PreguntaOrientadora`, `Registro` y `Retroalimentacion`
- [x] Repositorios `JpaRepository` (con la consulta de bitácora duplicada)
- [x] Servicios con las reglas de negocio
- [x] Controladores REST con los endpoints de la tabla
- [x] Manejo de errores (400 por validación, 404 si no existe, 409 si se rompe una regla)
- [x] Datos de prueba iniciales
- [x] Probar los endpoints con Postman