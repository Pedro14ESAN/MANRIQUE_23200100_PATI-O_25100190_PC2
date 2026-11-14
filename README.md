# Diario espacial – Práctica Calificada 2 (DAM 2026-2)

Aplicación Android en Kotlin y Jetpack Compose que consume la API abierta de la NASA (APOD y NeoWs) y guarda los datos en SQLite con Room.

## Integrantes

| Rol | Nombre | Opción |
|---|---|---|
| Alumno 1 | Pedro Luis | Imágenes (APOD) + base común |
| Alumno 2 | _(nombre del compañero)_ | Asteroides (NeoWs) |

## Cómo ejecutar el proyecto

1. Generar una clave en https://api.nasa.gov (sección "Generate API Key"). No usar `DEMO_KEY`.
2. Abrir el archivo `local.properties` en la raíz del proyecto (no se sube a GitHub) y agregar:
   ```
   NASA_API_KEY=tu_clave_aqui
   ```
3. Sincronizar Gradle y ejecutar el módulo `app` en un emulador o dispositivo (minSdk 33).

La clave se lee desde el código con `BuildConfig.NASA_API_KEY`, nunca se escribe en el código fuente.

## División del trabajo

### Alumno 1 – Pedro Luis
**Base común (primer PR, se fusiona antes que todo lo demás):**
- Dependencias en `libs.versions.toml`: Retrofit + conversor JSON, Coil, Room (KSP), Navigation Compose, ViewModel/Lifecycle.
- `buildConfigField` que lee `NASA_API_KEY` desde `local.properties`.
- `RetrofitInstance` con la URL base `https://api.nasa.gov/`.
- `AppDatabase` (clase Database de Room) con la tabla de APOD; el Alumno 2 agrega su Entity y DAO y sube la versión.
- Navegación principal: `Scaffold` con barra inferior (Imágenes / Asteroides) y un `NavHost`; la pestaña Asteroides arranca con una pantalla vacía que reemplaza el Alumno 2.

**Opción Imágenes (APOD):**
- `ApodApi` (`GET planetary/apod` con `start_date` y `end_date` de los últimos 7 días), `ApodDto`, `ApodEntity`, `ApodDao` y conversión DTO → Entity.
- `ApodRepository` y `ApodViewModel` con `StateFlow` (la pantalla siempre lee desde Room).
- Lista (más reciente primero; si `media_type` es `video`, texto "Contenido en video" sin cargar imagen), detalle (hdurl o url, título, fecha, explicación, copyright), botón de favorito y pantalla "Mis imágenes favoritas".
- Indicador de carga, mensaje cuando no hay datos y manejo de campos nulos.

### Alumno 2 – compañero
**Opción Asteroides (NeoWs):**
- `NeoWsApi` (`GET neo/rest/v1/feed`, rango de 7 días), DTOs del feed (el JSON viene agrupado por fecha: `Map<String, List<AsteroideDto>>`), `AsteroideEntity`, `AsteroideDao` y conversión DTO → Entity (velocidad y distancia llegan como texto y se convierten a número).
- Agregar la Entity y el DAO a `AppDatabase` (subir la versión).
- `AsteroideRepository` y `AsteroideViewModel` con `StateFlow`.
- Lista unificada ordenada por fecha de aproximación (más recientes primero) con nombre, fecha, diámetro máx. (m), velocidad (km/h) y distancia (km) redondeados a 2 decimales.
- Etiqueta o color para los potencialmente peligrosos e interruptor "Solo potencialmente peligrosos".
- Detalle (nombre, id, fecha, diámetro mín. y máx., velocidad, distancia, peligroso; opcional: NeoWs Lookup), botón de favorito y pantalla "Mis asteroides".
- Indicador de carga, mensaje cuando no hay datos y manejo de campos nulos.

**Ambos:** cada uno revisa y aprueba el PR del otro con al menos un comentario.

## Acuerdo de coordinación

- La rama `main` está protegida: nadie hace commits directos, todo entra por Pull Request.
- Ramas con el formato `feature/descripcion-corta`, por ejemplo `feature/base-comun`, `feature/opcion-imagenes`, `feature/opcion-asteroides`.
- **Pedro (Alumno 1) prepara la base común** (Retrofit, Database, navegación principal) en `feature/base-comun` y la fusiona primero, después de que el Alumno 2 la revise.
- **El Alumno 2 integra la base** actualizando su rama desde `main` (`git pull origin main`) y solo agrega su parte: sus archivos dentro de su paquete, su Entity y DAO en `AppDatabase` y su pantalla en la pestaña Asteroides del `NavHost`.
- Paquetes separados para no pisarse: `data/remote`, `data/local` y `ui/navigation` (común), `apod/` (Alumno 1) y `asteroides/` (Alumno 2).
- Commits pequeños con mensajes que expliquen el cambio.
- La clave de la API solo vive en `local.properties` de cada uno.
