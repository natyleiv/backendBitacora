# ms-bitacoras

Microservicio de bitácoras, preguntas orientadoras, registros de clase y retroalimentación de **Mi Bitácora**. Puerto **8082**, base de datos `colegio_bitacoras`.

**Responsable:** Juan Pablo Marambio Zagal

Los ids de `ms-usuarios` (`asignacionId`, `estudianteId`, `docenteId`) se guardan como números, sin clave foránea, porque viven en otra base de datos.

## Entidades por implementar (`model`)

| Entidad | Campos | Validaciones |
|---|---|---|
| `Bitacora` | `id`, `asignacionId`, `mes`, `anio`, `titulo`, `proposito`, `instrucciones`, `estado` (`BORRADOR` / `PUBLICADA` / `CERRADA`), `fechaPublicacion` | `titulo` de al menos 5 caracteres; `proposito` e `instrucciones` obligatorios; `mes` entre 1 y 12; `anio` entre 2025 y 2100 |
| `PreguntaOrientadora` | `id`, `bitacora`, `orden`, `texto` | Al menos una por bitácora; `texto` obligatorio |
| `Registro` | `id`, `bitacora`, `estudianteId`, `fechaClase`, `contenido`, `actividad`, `aprendizaje`, `dudas`, `enlace`, `fotoUri`, `estadoEnvio` (`BORRADOR` / `ENVIADO`) | `fechaClase` no futura y dentro del mes de la bitácora; `contenido` obligatorio; `actividad` y `aprendizaje` obligatorios al enviar; `enlace` opcional con `http://` o `https://` |
| `Retroalimentacion` | `id`, `registro` (1 a 1), `docenteId`, `comentario`, `fecha` | `comentario` de al menos 5 caracteres |

## Reglas de negocio

- No puede haber dos bitácoras con el mismo `asignacionId`, `mes` y `anio`.
- Solo se edita una bitácora en `BORRADOR`; solo se publica desde `BORRADOR` y solo se cierra desde `PUBLICADA`.
- Solo se puede registrar en una bitácora `PUBLICADA`.
- Un registro está "revisado" cuando tiene retroalimentación (no se guarda como columna aparte).

## Endpoints planificados (`controller`)

| Método | Ruta | Qué hace |
|---|---|---|
| `GET` / `POST` | `/bitacoras` | Listar / crear bitácoras (con sus preguntas) |
| `GET` / `PUT` / `DELETE` | `/bitacoras/{id}` | Ver / editar (solo borrador) / eliminar |
| `GET` | `/bitacoras/asignacion/{asignacionId}` | Bitácoras de una asignación (vista del docente) |
| `PATCH` | `/bitacoras/{id}/publicar` | Pasa de `BORRADOR` a `PUBLICADA` |
| `PATCH` | `/bitacoras/{id}/cerrar` | Pasa de `PUBLICADA` a `CERRADA` |
| `GET` / `POST` | `/registros` | Listar / crear registros |
| `GET` / `PUT` / `DELETE` | `/registros/{id}` | Ver / editar (solo borrador) / eliminar |
| `GET` | `/registros/bitacora/{bitacoraId}` | Aportes enviados de una bitácora (vista del docente) |
| `GET` | `/registros/estudiante/{estudianteId}` | Historial del estudiante |
| `POST` / `PUT` | `/registros/{id}/retroalimentacion` | Crear o actualizar el comentario del docente |

## Tareas

- [ ] Enums `EstadoBitacora` y `EstadoEnvio`
- [ ] Entidades `Bitacora`, `PreguntaOrientadora`, `Registro` y `Retroalimentacion`
- [ ] Repositorios `JpaRepository` (con la consulta de bitácora duplicada)
- [ ] Servicios con las reglas de negocio
- [ ] Controladores REST con los endpoints de la tabla
- [ ] Manejo de errores (400 por validación, 404 si no existe, 409 si se rompe una regla)
- [ ] Datos de prueba iniciales (las dos bitácoras de octubre y los dos registros de la app)
- [ ] Probar los endpoints con Postman
