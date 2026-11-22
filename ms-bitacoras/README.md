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
  "titulo": "Bitácora de Diciembre - Lenguaje",
  "proposito": "Registrar lo aprendido en Lenguaje durante diciembre.",
  "instrucciones": "Escribe un registro después de cada clase.",
  "mes": 12,