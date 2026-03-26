# MusicMood

> Registra tus canciones y el estado de ánimo que te generan.

Aplicacion web construida con **Ionic + Vue** en el frontend y **Spring Boot** en el backend, conectados mediante una API REST y respaldados por **PostgreSQL**. Todo el entorno corre en contenedores Docker.

---

## Stack tecnologico

| Capa | Tecnologia |
|---|---|
| Frontend | Ionic 7 + Vue 3 + Vite |
| Backend | Spring Boot 3 + Java 21 |
| Base de datos | PostgreSQL 16 |
| Infraestructura | Docker + Docker Compose |
| Comunicacion | API REST (JSON) |

---

## Estructura del proyecto

```
musicmood/
├── docker-compose.yml
├── backend-spring/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/musicmood/
│       ├── MusicMood.java               # Punto de entrada
│       ├── model/
│       │   ├── Song.java                # Entidad JPA
│       │   └── Mood.java                # Enum de moods
│       ├── repository/
│       │   └── SongRepository.java      # Patron Repository
│       ├── service/
│       │   └── SongService.java         # Logica de negocio
│       └── controller/
│           └── SongController.java      # Endpoints REST
└── frontend-ionic/
    ├── Dockerfile
    ├── package.json
    ├── vite.config.ts
    └── src/
        ├── main.ts                      # Setup Ionic + Vue
        ├── App.vue                      # Componente raiz
        ├── router/index.ts              # Rutas
        ├── services/SongService.ts      # Llamadas al API
        └── views/
            ├── HomePage.vue             # Lista + filtro por mood
            └── SongFormPage.vue         # Formulario crear/editar
```

---

## Requisitos

- [Docker](https://www.docker.com/) y Docker Compose instalados
- Eso es todo, no necesitas Java, Node ni nada mas

---

## Como levantar el proyecto

### Primera vez

```bash
# Clonar el repositorio
git clone <url-del-repo>
cd tarea_investigativa_4_ionic_y_spring

# Levantar todo
docker compose up --build
```

Esperar a que los tres servicios levanten. La primera vez tarda un poco porque Maven descarga las dependencias de Java y npm instala los paquetes de Node.

### Siguiente vez

```bash
docker compose up
```

### Bajar los contenedores

```bash
# Solo bajar
docker compose down

# Bajar y eliminar datos de la base de datos
docker compose down -v
```

---

## Acceso

Una vez levantado:

| Servicio | URL |
|---|---|
| App (frontend) | http://localhost:8100 |
| API (backend) | http://localhost:8080 |
| Base de datos | localhost:5432 |

---

## API REST

Base URL: `http://localhost:8080/api/songs`

| Metodo | Endpoint | Descripcion |
|---|---|---|
| GET | `/api/songs` | Obtener todas las canciones |
| GET | `/api/songs?mood=HAPPY` | Filtrar por mood |
| GET | `/api/songs/{id}` | Obtener una cancion |
| POST | `/api/songs` | Crear cancion |
| PUT | `/api/songs/{id}` | Editar cancion |
| DELETE | `/api/songs/{id}` | Eliminar cancion |

### Moods disponibles

`HAPPY` · `SAD` · `ENERGETIC` · `RELAXED` · `NOSTALGIC`

### Ejemplo de request

```json
POST /api/songs
{
  "title": "Blinding Lights",
  "artist": "The Weeknd",
  "genre": "Pop",
  "mood": "ENERGETIC",
  "notes": "Perfecta para manejar"
}
```

---

## Patrones implementados

**Spring Boot (backend)**
- **MVC** — separacion en Controller, Service, Repository
- **Inversion de Control / Inyeccion de Dependencias** — via `@Service`, `@Repository`, constructor injection
- **Repository** — `SongRepository` extiende `JpaRepository`
- **DTO implícito** — la entidad `Song` actua como contrato de datos con el frontend

**Ionic + Vue (frontend)**
- **Composicion de componentes** — cada vista es un componente Vue independiente
- **Reactivity** — `ref()` para estado local, `computed()` para valores derivados
- **Service layer** — `SongService.ts` centraliza todas las llamadas HTTP

---

## Credenciales de la base de datos

```
Host:     localhost
Puerto:   5432
Base:     musicmood_db
Usuario:  musicmood_user
Password: musicmood_pass
```

