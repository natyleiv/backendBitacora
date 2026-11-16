# ms-usuarios

Microservicio de usuarios, cursos, asignaturas y asignaciones de **Mi Bitácora**. Puerto **8081**, base de datos `colegio_usuarios` (se crea sola al ejecutar, con MySQL encendido en XAMPP).

**Responsable:** Natalia Millaray Leiva Millaqueo

## Estructura (`cl.duoc.colegio.usuarios`)

| Paquete | Contenido |
|---|---|
| `model` | `Curso`, `Asignatura`, `Usuario`, `Asignacion` y la clase de constantes `Perfil` (`DOCENTE` / `ESTUDIANTE`) |
| `repository` | Un `JpaRepository` por entidad, con consultas derivadas (`findByDocenteId`, `existsByCorreoIgnoreCase`, etc.) |
| `service` | Reglas de negocio. Cada servicio tiene un `validar()` que devuelve el mensaje de error o `null` |
| `controller` | Controladores REST con `ResponseEntity` (200, 201, 204, 400, 401, 404, 409) |
| `dto` | `LoginRequest` (lo que llega) y `LoginResponse` (lo que sale, sin contraseña) |
| `config` | `DataInitializer`: datos de prueba al arrancar |

## Entidades y reglas

| Entidad | Campos | Reglas |
|---|---|---|
| `Curso` | `id`, `nombre` (ej. "5°A") | Nombre de 2 a 20 caracteres y único. No se borra si tiene estudiantes o asignaciones (409) |
| `Asignatura` | `id`, `nombre` (ej. "Matemática") | Nombre de 3 a 50 caracteres y único. No se borra si tiene asignaciones (409) |
| `Usuario` | `id`, `nombre`, `correo`, `password`, `perfil`, `cursoId` | Nombre de al menos 3 caracteres; correo con `@`, terminado en `.cl` y único; contraseña de al menos 8 caracteres; perfil `DOCENTE` o `ESTUDIANTE`. El estudiante necesita un `cursoId` que exista; el docente no lleva curso. La contraseña se recibe pero **nunca se devuelve** |
| `Asignacion` | `id`, `docenteId`, `cursoId`, `asignaturaId` | El docente (con perfil `DOCENTE`), el curso y la asignatura deben existir. No se repite la misma combinación (409) |

Entre microservicios no hay claves foráneas: `ms-bitacoras` guarda `asignacionId`, `estudianteId` y `docenteId` como números.

## Endpoints

| Método | Ruta | Qué hace | Respuestas |
|---|---|---|---|
| `POST` | `/login` | Recibe `correo` y `password`; devuelve el usuario sin contraseña | 200, 400, 401 |
| `GET` / `POST` | `/cursos` | Listar / crear cursos | 200, 201, 400, 409 |
| `GET` / `PUT` / `DELETE` | `/cursos/{id}` | Ver / editar / eliminar un curso | 200, 204, 400, 404, 409 |
| `GET` / `POST` | `/asignaturas` | Listar / crear asignaturas | 200, 201, 400, 409 |
| `GET` / `PUT` / `DELETE` | `/asignaturas/{id}` | Ver / editar / eliminar una asignatura | 200, 204, 400, 404, 409 |
| `GET` / `POST` | `/usuarios` | Listar / crear usuarios | 200, 201, 400, 409 |
| `GET` / `PUT` / `DELETE` | `/usuarios/{id}` | Ver / editar / eliminar un usuario | 200, 204, 400, 404, 409 |
| `GET` | `/usuarios/curso/{cursoId}` | Estudiantes de un curso | 200 |
| `GET` / `POST` | `/asignaciones` | Listar / crear asignaciones | 200, 201, 400, 409 |
| `GET` / `DELETE` | `/asignaciones/{id}` | Ver / eliminar una asignación | 200, 204, 404 |
| `GET` | `/asignaciones/docente/{docenteId}` | Asignaciones de un docente (la app luego llama a `ms-bitacoras`) | 200 |
| `GET` | `/asignaciones/curso/{cursoId}` | Asignaciones de un curso (el estudiante ve las bitácoras de su curso) | 200 |

### Ejemplos de JSON

Login (`POST /login`):

```json
{ "correo": "ana@colegio.cl", "password": "docente123" }
```

Respuesta (sin contraseña):

```json
{ "id": 1, "nombre": "Ana", "correo": "ana@colegio.cl", "perfil": "DOCENTE", "cursoId": null }
```

Crear estudiante (`POST /usuarios`):

```json
{ "nombre": "Estudiante Seis", "correo": "estudiante6@colegio.cl", "password": "alumno123", "perfil": "ESTUDIANTE", "cursoId": 1 }
```

Crear asignación (`POST /asignaciones`):

```json
{ "docenteId": 1, "cursoId": 1, "asignaturaId": 1 }
```

## Datos de prueba

Se cargan solos si la tabla de usuarios está vacía. **Los ids coinciden con los que usa `ms-bitacoras`**, por eso se guardan en este orden.

| Id | Usuarios | Curso | Contraseña |
|---|---|---|---|
| 1 | Ana (docente) – `ana@colegio.cl` | – | `docente123` |
| 2 | Luis (docente) – `luis@colegio.cl` | – | `docente123` |
| 3 | Estudiante Uno – `estudiante1@colegio.cl` | 5°A | `alumno123` |
| 4 | Estudiante Dos – `estudiante2@colegio.cl` | 5°A | `alumno123` |
| 5 | Estudiante Tres – `estudiante3@colegio.cl` | 5°A | `alumno123` |
| 6 | Estudiante Cuatro – `estudiante4@colegio.cl` | 5°A | `alumno123` |
| 7 | Estudiante Cinco – `estudiante5@colegio.cl` | 6°B | `alumno123` |

| Id | Asignación |
|---|---|
| 1 | Ana, 5°A, Matemática |
| 2 | Ana, 5°A, Lenguaje |
| 3 | Luis, 6°B, Historia |

Cursos: 1 = 5°A, 2 = 6°B. Asignaturas: 1 = Matemática, 2 = Lenguaje, 3 = Historia.

## Cómo ejecutar

1. Encender MySQL en XAMPP.
2. Abrir la carpeta `ms-usuarios` como proyecto aparte en IntelliJ y ejecutar `MsUsuariosApplication`, o desde la terminal: `./mvnw spring-boot:run`.
3. Probar en Postman con `http://localhost:8081`.

## Tareas

- [x] Entidades `Curso`, `Asignatura`, `Usuario` y `Asignacion` (más constantes `Perfil`)
- [x] Repositorios `JpaRepository` de cada entidad
- [x] Servicios con las reglas de negocio (correo único, asignación no repetida)
- [x] Controladores REST con los endpoints de la tabla
- [x] DTO de login (`LoginRequest`) y de respuesta sin contraseña
- [x] Datos de prueba iniciales (2 docentes, 5 estudiantes, 5°A y 6°B, 3 asignaturas, 3 asignaciones)
- [x] Probar los endpoints con Postman
