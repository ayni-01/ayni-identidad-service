# 🔐 Ayni Identidad Service

Microservicio de autenticación y gestión de usuarios para la plataforma Somos Ayni. Es el único servicio que firma tokens JWT; todos los demás microservicios del ecosistema solo los verifican. Existe como dominio separado porque la identidad y el control de acceso deben estar aislados del resto del negocio, evitando que cambios en otros contextos afecten la seguridad del sistema.

## Responsabilidad del Bounded Context

**Maneja:**
- Registro de nuevos usuarios (talentos, empresas y administradores)
- Autenticación y emisión de tokens JWT firmados
- Cambio de contraseña

**No maneja:**
- Perfiles de talento ni datos profesionales (responsabilidad de `ayni-perfiles-service`)
- Retos ni postulaciones
- Insignias ni gamificación
- Solo sabe "quién" es el usuario — no qué ha hecho ni qué habilidades tiene

---

## Endpoints REST

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| POST | `/api/v1/auth/registro` | Registra un nuevo usuario (TALENTO / EMPRESA / ADMIN) | Público |
| POST | `/api/v1/auth/login` | Inicia sesión y retorna un JWT firmado | Público |
| POST | `/api/v1/auth/password` | Cambia la contraseña del usuario autenticado | JWT requerido |

> Todos los endpoints bajo `/api/v1/auth/**` son públicos excepto el cambio de contraseña.

---

## Arquitectura (Hexagonal)

```
src/main/java/com/somosayni/identidad/
├── domain/           # Núcleo del negocio — sin dependencias externas
│   ├── model/        # Entidades y enums (Usuario, Rol)
│   └── port/         # Interfaces de repositorio y servicios de salida
├── application/      # Casos de uso (registro, login, cambio de contraseña)
│   └── service/      # Orquestación de la lógica de negocio
└── infrastructure/   # Adaptadores técnicos
    ├── persistence/  # JPA repositories, entidades de BD
    ├── security/     # Filtros JWT, configuración Spring Security
    └── web/          # Controladores REST, DTOs de entrada/salida
```

| Capa | Responsabilidad |
|------|----------------|
| **domain** | Reglas de negocio puras: validación de credenciales, generación de claims JWT, políticas de roles |
| **application** | Orquesta los puertos: llama al repositorio, invoca el generador de token, devuelve la respuesta |
| **infrastructure** | Todo lo técnico: Spring Boot, JPA/PostgreSQL, filtros de seguridad, serialización JSON |

---

## Modelos de dominio principales

### Usuario

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | UUID | Identificador único del usuario |
| `email` | String | Correo electrónico (único en el sistema) |
| `passwordHash` | String | Contraseña encriptada con BCrypt |
| `rol` | Enum | `TALENTO` / `EMPRESA` / `ADMIN` |
| `activo` | Boolean | Si el usuario puede iniciar sesión |

### Token JWT emitido

El token incluye los siguientes claims:
- `sub` — `userId` (UUID del usuario)
- `email` — correo electrónico
- `rol` — rol del usuario
- Expiración: **24 horas**

---

## Cómo ejecutar

### Local

```bash
# Requisitos: Java 21, Maven, PostgreSQL corriendo en localhost:5432
mvn clean package -DskipTests
java -jar target/*.jar
```

### Docker

```bash
cp .env.example .env
# Editar .env con tus valores reales
docker-compose up --build
```

---

## Variables de entorno

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `SERVER_PORT` | Puerto en que levanta el servicio | `8081` |
| `DB_HOST` | Host de PostgreSQL | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base de datos | `somosayni` |
| `DB_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | *(requerido)* |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | *(requerido — compartida con todos los servicios)* |
| `JWT_EXPIRATION_MS` | Duración del token en milisegundos | `86400000` (24 h) |

---

## Swagger / OpenAPI

| | Link |
|---|---|
| **Swagger UI (local)** | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |
| **OpenAPI JSON (local)** | [http://localhost:8081/api-docs](http://localhost:8081/api-docs) |
| **swagger.json (repo)** | [ver en GitHub](https://github.com/ayni-01/ayni-identidad-service/blob/main/swagger.json) |
| **Swagger Editor (online)** | [abrir en Swagger Editor](https://editor.swagger.io/?url=https://raw.githubusercontent.com/ayni-01/ayni-identidad-service/main/swagger.json) |

> Para probar los endpoints protegidos: copia el JWT del login → clic en **Authorize** → pega `Bearer <tu-token>`.

---

## Dependencias

Este servicio forma parte del ecosistema Somos Ayni junto con `ayni-habilidades-service`, `ayni-postulaciones-service`, `ayni-perfiles-service` y `ayni-retos-service`. Los servicios comparten las siguientes convenciones:

- **Base de datos:** Todos usan la misma instancia de PostgreSQL, base de datos `somosayni`, pero cada servicio opera sobre sus propias tablas sin hacer JOINs entre contextos.
- **JWT:** Este es el **único servicio que firma tokens**. El resto de los servicios solo validan la firma usando la misma variable `JWT_SECRET`.
- **Comunicación:** Los servicios son independientes entre sí. Se referencian únicamente por ID (p. ej. `talentoId`, `retoId`) y no realizan llamadas HTTP entre ellos en tiempo real.
