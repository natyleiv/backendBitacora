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

_(Se completa a medida que se implementan. El detalle planificado está en el README de cada microservicio.)_

## Estado

Avance: estructura base de los dos microservicios, con su configuración y sus paquetes (`model`, `repository`, `service`, `controller`, `dto`).
