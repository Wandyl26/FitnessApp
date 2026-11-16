# FitnessApp

Aplicación Android desarrollada con **Jetpack Compose** que consume dos microservicios REST (**UserService** y **AuthService**) para el registro de usuarios y la autenticación mediante JWT. El token de sesión se persiste de forma local con **Room**, y el proyecto sigue los principios de **Clean Architecture** y el patrón **MVVM**.

---

## 📑 Tabla de contenidos

- [Descripción](#-descripción)
- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Stack tecnológico](#-stack-tecnológico)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Requisitos previos](#-requisitos-previos)
- [Configuración de microservicios](#-configuración-de-microservicios)
- [Endpoints consumidos](#-endpoints-consumidos)
- [Pantallas](#-pantallas)
- [Manejo de errores](#-manejo-de-errores)
- [Persistencia del token](#-persistencia-del-token)
- [Cómo ejecutar el proyecto](#-cómo-ejecutar-el-proyecto)
- [Decisiones de diseño](#-decisiones-de-diseño)
- [Autor](#-autor)

---

## 📖 Descripción

**FitnessApp** es una aplicación móvil que permite a los usuarios:

1. **Registrarse** en el sistema mediante el microservicio `UserService`.
2. **Iniciar sesión** a través del microservicio `AuthService`.
3. **Persistir el token JWT** devuelto por el servicio de autenticación, almacenándolo de forma segura en una base de datos local con **Room**.
4. Navegar entre las pantallas de **Login** y **Registro** mediante **Navigation Compose**, con una interfaz moderna construida íntegramente en **Jetpack Compose** y **Material 3**.

El proyecto está estructurado siguiendo los principios de **Clean Architecture**, separando claramente las capas de **dominio**, **infraestructura** y **presentación**, y aplicando el patrón **MVVM** en la capa de UI.

---

## ✨ Características

- ✅ Registro de usuarios con validación en backend.
- ✅ Inicio de sesión con credenciales.
- ✅ Persistencia del token JWT en **Room**.
- ✅ Arquitectura limpia (domain / infrastructure / app).
- ✅ Patrón MVVM con `StateFlow` para el manejo de estado.
- ✅ Inyección de dependencias con **Hilt**.
- ✅ Consumo de API con **Retrofit** + **Gson** + **OkHttp**.
- ✅ UI declarativa con **Jetpack Compose** y **Material 3**.
- ✅ Navegación entre pantallas con **Navigation Compose**.
- ✅ Manejo de errores del backend (estructura `ProblemDetail`: `title`, `status`, `detail`).
- ✅ Diseño elegante con degradados, tarjetas elevadas y campos estilizados.

---

## 🏛️ Arquitectura

El proyecto sigue los principios de **Clean Architecture**, dividiendo el código en tres módulos principales:

```
┌─────────────────────────────────────────────┐
│                    :app                     │  ← Presentación (Compose + MVVM)
│  ViewModels · Screens · Navigation          │
└──────────────────┬──────────────────────────┘
                   │ depende de
┌──────────────────▼──────────────────────────┐
│                  :domain                    │  ← Reglas de negocio (Kotlin puro)
│  Models · Repository Interfaces · Services  │
└──────────────────▲──────────────────────────┘
                   │ implementado por
┌──────────────────┴──────────────────────────┐
│              :infrastructure                │  ← Detalles técnicos
│  Retrofit · DTOs · Room · RepositoryImpl    │
└─────────────────────────────────────────────┘
```

### Principios aplicados

- **Inversión de dependencias**: `domain` define interfaces (`AuthRepository`), e `infrastructure` las implementa (`AuthRepositoryImpl`).
- **Aislamiento del dominio**: `domain` es Kotlin puro, sin dependencias de Android ni frameworks.
- **Separación de modelos**: los modelos de dominio (`LoginRequest`, `RegisterRequest`, `LoginResult`, `RegisterResult`) son independientes de los DTOs de red (`LoginRequestDto`, `LoginResponseDto`, etc.).
- **Mapeo explícito**: la capa de infraestructura es la única que conoce tanto los modelos de dominio como los DTOs, y se encarga de la conversión entre ellos.

### Flujo de una petición

```
UI (Screen) → ViewModel → AuthService (domain) → AuthRepository (interface)
    → AuthRepositoryImpl (infrastructure) → Retrofit/Room → Backend/Local DB
```

---

## 🛠️ Stack tecnológico

| Categoría | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navegación | Navigation Compose |
| Patrón de presentación | MVVM + `StateFlow` |
| Inyección de dependencias | Hilt |
| Consumo de API | Retrofit + Gson + OkHttp |
| Persistencia local | Room |
| Concurrencia | Coroutines + Flow |
| Build System | Gradle (Kotlin DSL) + `libs.versions.toml` |
| Procesador de anotaciones | KSP |
| SDK mínimo | API 24 |
| `compileSdk` / `targetSdk` | 36 |
| AGP | 8.9.3 |

---

## 📂 Estructura del proyecto

```
FitnessApp/
├── app/                                  # Capa de presentación
│   └── src/main/java/com/example/fitnessapp/
│       ├── ViewModel/
│       │   ├── LoginViewModel.kt
│       │   └── RegisterViewModel.kt
│       ├── view/
│       │   ├── MainActivity.kt
│       │   ├── Navigation.kt
│       │   └── Screen/
│       │       ├── LoginScreen.kt
│       │       └── RegisterScreen.kt
│       └── ui/theme/                     # Tema de la app
│
├── domain/                               # Reglas de negocio (Kotlin puro)
│   └── src/main/java/com/example/domain/
│       ├── model/
│       │   ├── LoginRequest.kt
│       │   ├── LoginResult.kt
│       │   ├── RegisterRequest.kt
│       │   └── RegisterResult.kt
│       ├── repository/
│       │   └── AuthRepository.kt
│       └── service/
│           └── AuthService.kt
│
├── infrastructure/                       # Detalles técnicos
│   └── src/main/java/com/example/infrastructure/
│       ├── api/
│       │   ├── AuthApi.kt
│       │   ├── UserApi.kt
│       │   └── dto/
│       │       ├── ApiErrorDto.kt
│       │       ├── LoginRequestDto.kt
│       │       ├── LoginResponseDto.kt
│       │       ├── RegisterRequestDto.kt
│       │       └── RegisterResponseDto.kt
│       ├── local/
│       │   ├── AppDatabase.kt
│       │   ├── dao/
│       │   │   └── AuthTokenDao.kt
│       │   └── entity/
│       │       └── AuthToken.kt
│       ├── modules/
│       │   ├── AuthModule.kt
│       │   ├── AuthNetworkModule.kt
│       │   ├── DatabaseModule.kt
│       │   └── Qualifiers.kt
│       └── service/
│           └── AuthRepositoryImpl.kt
│
├── gradle/[README.md](../Login/fitness-microservices-vs/README.md)
│   └── libs.versions.toml                # Catálogo de versiones
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## ✅ Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener:

- **Android Studio** Ladybug (2024.2.1) o superior.
- **JDK 17** o superior.
- **Android SDK** con `compileSdk 36` instalado.
- **Gradle** compatible con AGP 8.9.3 (el wrapper del proyecto lo gestiona automáticamente).
- Los dos microservicios en ejecución **en la máquina host** (ver la siguiente sección).

---

## 🔌 Configuración de microservicios

La aplicación consume **dos microservicios** independientes que deben estar levantados en la máquina host:

| Servicio | Puerto | Base URL desde el emulador |
|---|---|---|
| **UserService** | `8081` | `http://10.0.2.2:8081/` |
| **AuthService** | `8082` | `http://10.0.2.2:8082/` |

> ⚠️ **Importante**: `localhost` dentro del emulador de Android apunta al propio dispositivo virtual, no al host. Por eso la app utiliza `10.0.2.2`, que es la dirección especial del host desde el emulador.

Las URLs están definidas en:

```
infrastructure/src/main/java/com/example/infrastructure/modules/AuthNetworkModule.kt
```

```kotlin
private const val BASE_URL = "http://10.0.2.2"
```

Si ejecutas la app en un **dispositivo físico**, cambia `10.0.2.2` por la **IP local de tu máquina host** (por ejemplo, `http://192.168.1.50:8081/`), y asegúrate de que ambos estén en la misma red.

---

## 🌐 Endpoints consumidos

### A. Registrar usuario — `UserService`

**`POST /api/v1/users/register`**

**Request body:**
```json
{
  "identityDocument": "1098765432",
  "fullName": "Wandyl Diaz",
  "username": "wdiaz",
  "email": "wandyl@test.com",
  "password": "secret123",
  "weight": 75.5,
  "height": 1.78
}
```

**Response `201 Created`:**
```json
{
  "id": 1,
  "username": "wdiaz",
  "email": "wandyl@test.com",
  "message": "Usuario registrado exitosamente"
}
```

**Error `409 Conflict`:**
```json
{
  "title": "Conflict",
  "status": 409,
  "detail": "El usuario o correo ya se encuentra registrado."
}
```

---

### B. Iniciar sesión — `AuthService`

**`POST /api/v1/auth/login`**

**Request body:**
```json
{
  "username": "wdiaz",
  "password": "secret123"
}
```

**Response `200 OK`:**
```json
{
  "token": "mock-jwt-token-dotnet-2026",
  "username": "wdiaz",
  "message": "Autenticación exitosa"
}
```

**Error `401 Unauthorized`:**
```json
{
  "title": "Unauthorized",
  "status": 401,
  "detail": "Credenciales inválidas"
}
```

---

## 🎨 Pantallas

### Pantalla de Login
- Fondo con degradado (`#667EEA` → `#764BA2`).
- Tarjeta elevada con esquinas redondeadas (`RoundedCornerShape(24.dp)`).
- Campos `OutlinedTextField` con iconos (`Person`, `Lock`).
- Campo de contraseña con `PasswordVisualTransformation`.
- Indicador de carga (`CircularProgressIndicator`) durante la petición.
- Manejo de errores visible bajo el botón.
- Botón **"¿No tienes cuenta? Regístrate"** para navegar al registro.

### Pantalla de Registro
- Mismo estilo visual que la pantalla de login.
- Formulario con `verticalScroll` para soportar pantallas pequeñas.
- Campos: `identityDocument`, `fullName`, `username`, `email`, `password`, `weight`, `height`.
- Conversión segura de `weight` y `height` a `Double` (con `toDoubleOrNull()`).
- Al registrarse con éxito, regresa automáticamente a la pantalla de login.

---

## 🚨 Manejo de errores

El backend devuelve errores con la estructura **ProblemDetails**:

```json
{
  "title": "Conflict",
  "status": 409,
  "detail": "El usuario o correo ya se encuentra registrado."
}
```

En la capa de infraestructura, estos errores se parsean usando `ApiErrorDto` y se transforman en un `Result.failure(Exception(detail))`, cuyo mensaje es consumido por el `ViewModel` y mostrado en la UI:

```kotlin
val errorBody = response.errorBody()?.string()
val apiError = gson.fromJson(errorBody, ApiErrorDto::class.java)
Result.failure(Exception(apiError?.detail ?: "Error desconocido"))
```

Esto permite mostrar mensajes **exactos del backend** al usuario, sin exponer detalles técnicos.

---

## 💾 Persistencia del token

El token JWT se almacena en una base de datos local **Room** con una única fila (id = 1), garantizando que solo exista una sesión activa a la vez.

### Esquema

```
┌───────────────────────────────┐
│          auth_token           │
├───────────────────────────────┤
│ id           INTEGER (PK = 1) │
│ token        TEXT             │
│ username     TEXT             │
└───────────────────────────────┘
```

### Flujo

1. Al hacer login exitoso, `AuthRepositoryImpl` guarda el token:
   ```kotlin
   authTokenDao.insertToken(AuthToken(token = body.token, username = body.username))
   ```
2. En cualquier punto, se puede recuperar con `authTokenDao.getToken()`.
3. Al cerrar sesión, `authTokenDao.clearToken()` elimina el registro.

### Verificación

Puedes inspeccionar el token guardado abriendo:

**Android Studio → View → Tool Windows → App Inspection → Database Inspector**

y navegando a la base de datos `auth_db`, tabla `auth_token`.

---

## 🚀 Cómo ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/Wandyl26/FitnessApp.git
cd FitnessApp
```

### 2. Levantar los microservicios

Asegúrate de que `UserService` esté corriendo en el puerto `8081` y `AuthService` en el `8082` en la máquina host.

### 3. Sincronizar Gradle

Abre el proyecto en **Android Studio** y espera a que se complete la sincronización (o ejecuta `./gradlew sync`).

### 4. Ejecutar la app

- Selecciona un emulador o dispositivo físico.
- Haz clic en **Run ▶**.

### 5. Probar el flujo

1. Abre la app → verás la pantalla de **Login**.
2. Pulsa **"¿No tienes cuenta? Regístrate"**.
3. Rellena el formulario con datos válidos:
    - `identityDocument`: `1098765432`
    - `fullName`: `Wandyl Diaz`
    - `username`: `wdiaz`
    - `email`: `wandyl@test.com`
    - `password`: `secret123`
    - `weight`: `75.5`
    - `height`: `1.78`
4. Al registrarte con éxito, regresarás al login.
5. Inicia sesión con `wdiaz` / `secret123`.
6. Verifica el token en el **Database Inspector**.

---

## 🧠 Decisiones de diseño

| Decisión | Justificación |
|---|---|
| **Módulos separados (`domain`, `infrastructure`, `app`)** | Refuerza los límites entre capas en tiempo de compilación, no solo por convención. |
| **`LoginRequest` y `RegisterRequest` en `domain`** | El dominio expone contratos de entrada como objetos inmutables en lugar de parámetros sueltos, facilitando la evolución del contrato. |
| **DTOs con sufijo `Dto`** | Evita colisiones con los modelos de dominio y hace explícito qué pertenece a la capa de red. |
| **Room en `infrastructure`** | Es un detalle de implementación de persistencia; el dominio no debe conocerlo. |
| **Dos instancias de Retrofit** | Los microservicios están en puertos distintos, y cada uno tiene su propio `baseUrl`. Se distinguen con `@Qualifier`. |
| **`Result<T>` en el repositorio** | Permite modelar éxito/error sin lanzar excepciones al ViewModel y mantiene el flujo funcional. |
| **`StateFlow` en los ViewModels** | Es la forma recomendada por Google para exponer estado a Compose, con soporte para `collectAsState()`. |
| **Compose BOM** | Garantiza compatibilidad entre todas las librerías de Compose y evita conflictos de versiones transitivas. |
| **KSP en lugar de KAPT** | Es la vía recomendada por Google desde Kotlin 2.0, más rápida y con mejor soporte a futuro. |

---

## 👤 Autor

**Wandyl Diaz**

- GitHub: [@Wandyl26](https://github.com/Wandyl26)

---
