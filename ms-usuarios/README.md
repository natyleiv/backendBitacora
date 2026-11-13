# ms-usuarios

Microservicio de usuarios, cursos, asignaturas y asignaciones de **Mi Bitácora**. Puerto **8081**, base de datos `colegio_usuarios`.

**Responsable:** Natalia Millaray Leiva Millaqueo

## Entidades por implementar (`model`)

| Entidad | Campos | Validaciones |
|---|---|---|
| `Curso` | `id`, `nombre` (ej. "5°A") | `nombre` obligatorio y único |
| `Asignatura` | `id`, `nombre` (ej. "Matemática") | `nombre` obligatorio y único |
| `Usuario` | `id`, `nombre`, `correo`, `password`, `perfil` (`DOCENTE` / `ESTUDIANTE`), `curso` (solo estudiantes) | `correo` único, con `@` y terminado en `.cl`; `password` de al menos 8 caracteres |
| `Asignacion` | `id`, `docente` (Usuario), `curso`, `asignatura` | No repetir la misma combinación docente–curso–asignatura |

## Endpoints planificados (`controller`)

| Método | Ruta | Qué hace |
|---|---|---|
| `POST` | `/auth/login` | Recibe correo y contraseña; devuelve el usuario (sin contraseña) o 401 |
| `GET` / `POST` | `/cursos` | Listar / crear cursos |
| `GET` / `PUT` / `DELETE` | `/cursos/{id}` | Ver / editar / eliminar un curso |
| `GET` / `POST` | `/asignaturas` | Listar / crear asignaturas |
| `GET` / `PUT` / `DELETE` | `/asignaturas/{id}` | Ver / editar / eliminar una asignatura |
| `GET` / `POST` | `/usuarios` | Listar / crear usuarios |
| `GET` / `PUT` / `DELETE` | `/usuarios/{id}` | Ver / editar / eliminar un usuario |
| `GET` | `/usuarios/curso/{cursoId}` | Estudiantes de un curso (para "Sin participación") |
| `GET` / `POST` | `/asignaciones` | Listar / crear asignaciones |
| `GET` | `/asignaciones/docente/{docenteId}` | Cursos y asignaturas de un docente |
| `DELETE` | `/asignaciones/{id}` | Eliminar una asignación |

## Tareas

- [ ] Entidades `Curso`, `Asignatura`, `Usuario` (con enum `Perfil`) y `Asignacion`
- [ ] Repositorios `JpaRepository` de cada entidad
- [ ] Servicios con las reglas de negocio (correo único, asignación no repetida)
- [ ] Controladores REST con los endpoints de la tabla
- [ ] DTO de login (`LoginRequest`) y de respuesta sin contraseña
- [ ] Datos de prueba iniciales (los mismos de la app: 2 docentes, 5 estudiantes, 5°A y 6°B, 3 asignaturas)
- [ ] Probar los endpoints con Postman
