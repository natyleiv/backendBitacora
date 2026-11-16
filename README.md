# Mi Bitácora – Backend (microservicios)

Backend de la app **Mi Bitácora** (Bitácora Escolar), desarrollado en Spring Boot para la Evaluación Parcial 4 de DSY1105.

La app móvil (Kotlin + Jetpack Compose) está en el repositorio [natyleiv/proyectoColegio](https://github.com/natyleiv/proyectoColegio).

## Integrantes

- Natalia Millaray Leiva Millaqueo
- Juan Pablo Marambio Zagal

## Microservicios

| Microservicio | Responsable | Puerto | Base de datos | Qué maneja |
|---|---|---|---|---|
| [`ms-usuarios`](ms-usuarios) | Natalia | 8081 | `colegio_usuarios` | Usuarios, cursos, asignaturas, asignaciones docente–curso–asignatura y login |
| [`ms-bitacoras`](ms-bitacoras) | Juan Pablo | 8082 | `colegio_bitacoras` | Bitácoras, preguntas orientadoras, registros de clase y retroalimentación |

Cada microservicio tiene su propia base de datos. Entre ellos no hay claves foráneas: `ms-bitacoras` guarda los ids de `ms-usuarios` (`asignacionId`, `estudianteId`, `docenteId`) como números.

## Tecnologías

- Java 21 y Spring Boot 4.1.2
- Spring Web MVC, Spring Data JPA y Bean Validation
- MySQL
- Lombok

## Pasos para ejecutar

1. Encender MySQL en el puerto 3306 (XAMPP, Laragon o similar). Las bases de datos se crean solas al iniciar.
2. Si tu MySQL tiene contraseña, cámbiala en `src/main/resources/application.properties` de cada microservicio.
3. Abrir cada carpeta (`ms-usuarios` y `ms-bitacoras`) como un proyecto aparte en IntelliJ IDEA o VS Code y ejecutar su clase `...Application`, o desde la terminal:

```
cd ms-usuarios
./mvnw spring-boot:run
```

(En Windows: `mvnw.cmd spring-boot:run`.) Repetir en otra terminal con `ms-bitacoras`.

## Endpoints

### ms-bitacoras (puerto 8082)

Detalle completo, con reglas y ejemplos de JSON, en [`ms-bitacoras/README.md`](ms-bitacoras/README.md).

| Recurso | Rutas |
|---|---|
| Bitácoras | `GET/POST /bitacoras`, `GET/PUT/DELETE /bitacoras/{id}`, `GET /bitacoras/asignacion/{asignacionId}`, `PATCH /bitacoras/{id}/publicar`, `PATCH /bitacoras/{id}/cerrar` |
| Preguntas orientadoras | `GET/PUT /bitacoras/{bitacoraId}/preguntas` |
| Registros | `POST /registros`, `GET/PUT/DELETE /registros/{id}`, `GET /registros/bitacora/{bitacoraId}`, `GET /registros/estudiante/{estudianteId}` |
| Retroalimentación | `GET/PUT /registros/{registroId}/retroalimentacion` |

### ms-usuarios (puerto 8081)

Detalle completo, con reglas, ejemplos de JSON y datos de prueba, en [`ms-usuarios/README.md`](ms-usuarios/README.md).

| Recurso | Rutas |
|---|---|
| Login | `POST /login` |
| Cursos | `GET/POST /cursos`, `GET/PUT/DELETE /cursos/{id}` |
| Asignaturas | `GET/POST /asignaturas`, `GET/PUT/DELETE /asignaturas/{id}` |
| Usuarios | `GET/POST /usuarios`, `GET/PUT/DELETE /usuarios/{id}`, `GET /usuarios/curso/{cursoId}` |
| Asignaciones | `GET/POST /asignaciones`, `GET/DELETE /asignaciones/{id}`, `GET /asignaciones/docente/{docenteId}`, `GET /asignaciones/curso/{cursoId}` |

## Estado

- **ms-bitacoras:** completo. Entidades, repositorios, servicios con validaciones y reglas de negocio, controladores REST, datos de prueba y endpoints probados con Postman.
- **ms-usuarios:** completo. Entidades, repositorios, servicios con `validar()` y reglas de negocio, controladores REST, login sin contraseña y datos de prueba con los mismos ids que usa `ms-bitacoras`. Endpoints probados con Postman.
