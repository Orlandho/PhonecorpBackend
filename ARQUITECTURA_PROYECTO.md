# PhoneCorp Backend — Documentación de Arquitectura

> Spring Boot 3.2.2 · Java 21 · SQL Server · Maven

---

## 1. Descripción General

**PhoneCorp Backend** es una API REST que gestiona el ciclo completo de ventas de equipos celulares. Abarca desde el registro de clientes con validación de identidad (RENIEC), pasando por el catálogo de productos, creación de órdenes de venta, procesamiento de pagos con emisión de comprobantes (SUNAT), despacho con control de inventario, hasta la gestión de postventa.

La aplicación corre en el puerto **8080** por defecto y está conectada a una base de datos **SQL Server** local llamada `DB_PhoneCorp`.

---

## 2. Arquitectura: BCE (Boundary-Control-Entity)

El proyecto sigue el patrón arquitectónico **BCE**, que separa las responsabilidades en tres capas:

```
HTTP Request
     │
     ▼
┌─────────────────────────────────────────────────────────┐
│  BOUNDARY — Controladores (@RestController)             │
│  Reciben la request, validan formato y delegan al       │
│  servicio. No contienen lógica de negocio.              │
└───────────────────────┬─────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────┐
│  CONTROL — Servicios (@Service)                         │
│  Contienen toda la lógica de negocio: validaciones,     │
│  cálculos, orquestación de repositorios.                │
└───────────────────────┬─────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────┐
│  ENTITY — Repositorios + Entidades JPA                  │
│  Acceso a datos. JpaRepository maneja el CRUD.          │
│  Las entidades mapean tablas de la base de datos.       │
└─────────────────────────────────────────────────────────┘
                        │
                        ▼
               Base de datos SQL Server
                   (DB_PhoneCorp)
```

**Capas de soporte (transversales):**
- `dto/` — Objetos de transferencia (entrada/salida de la API)
- `exception/` — Manejo centralizado de errores
- `config/` — Configuraciones globales (CORS, seguridad, beans)

---

## 3. Estructura de Directorios Completa

```
PhonecorpBackend/
├── pom.xml                                         ← Configuración Maven y dependencias
├── API_DOCS.md                                     ← Documentación de API (ejemplos JSON)
├── ARQUITECTURA_PROYECTO.md                        ← Este documento
├── basededatos/
│   └── createDB.sql                                ← Script de creación de la BD
└── src/
    └── main/
        ├── java/
        │   ├── com/phonecorp/phonecorpbackend/
        │   │   └── PhonecorpBackend.java           ← Clase principal (punto de entrada)
        │   ├── config/
        │   │   ├── ConexionBD.java                 ← Singleton JDBC (legado)
        │   │   ├── CorsConfig.java                 ← Configuración CORS
        │   │   ├── MapperConfig.java               ← Bean ModelMapper
        │   │   ├── RestTemplateConfig.java         ← Cliente HTTP (RENIEC/SUNAT)
        │   │   └── SecurityConfig.java             ← Seguridad HTTP (en desarrollo)
        │   ├── controller/
        │   │   ├── CatalogoController.java         ← Endpoints de productos
        │   │   ├── ClienteController.java          ← Endpoints de clientes
        │   │   ├── ComprobanteController.java      ← Endpoints de comprobantes
        │   │   ├── InventarioDespachoController.java ← Endpoints de despacho/stock
        │   │   ├── OrdenVentaController.java       ← Endpoints de órdenes
        │   │   ├── PagoFacturacionController.java  ← Endpoints de pagos
        │   │   └── PostventaController.java        ← Endpoints de postventa
        │   ├── domain/
        │   │   ├── EntidadCliente.java             ← Tabla: Cliente
        │   │   ├── EntidadProducto.java            ← Tabla: Producto
        │   │   ├── EntidadInventario.java          ← Tabla: Inventario
        │   │   ├── EntidadOrdenVenta.java          ← Tabla: OrdenVenta
        │   │   ├── EntidadDetalleOrden.java        ← Tabla: DetalleOrden
        │   │   ├── EntidadPago.java                ← Tabla: Pago
        │   │   ├── EntidadComprobante.java         ← Tabla: Comprobante
        │   │   └── EntidadTicketPostventa.java     ← Tabla: TicketPostventa
        │   ├── dto/
        │   │   ├── ClienteRequestDTO.java          ← Entrada: registrar/actualizar cliente
        │   │   ├── ClienteResponseDTO.java         ← Salida: datos de cliente
        │   │   ├── ProductoResponseDTO.java        ← Salida: producto con stock
        │   │   ├── OrdenVentaRequestDTO.java       ← Entrada: crear orden
        │   │   ├── OrdenVentaResponseDTO.java      ← Salida: orden con desglose
        │   │   ├── ItemOrdenRequest.java           ← Ítem individual de una orden
        │   │   ├── PagoRequestDTO.java             ← Entrada: procesar pago
        │   │   ├── PagoResponseDTO.java            ← Salida: pago + comprobante
        │   │   ├── TicketPostventaRequestDTO.java  ← Entrada: registrar ticket
        │   │   ├── TicketPostventaResponseDTO.java ← Salida: datos del ticket
        │   │   └── ApiErrorDTO.java                ← Respuesta estándar de error
        │   ├── exception/
        │   │   ├── GlobalExceptionHandler.java     ← Manejador central de excepciones
        │   │   ├── DniInvalidoException.java       ← Error: DNI no validado por RENIEC
        │   │   ├── StockInsuficienteException.java ← Error: sin stock para la cantidad
        │   │   └── HistorialCrediticioBloqueadoException.java ← Error: cliente con crédito MALO
        │   ├── repository/
        │   │   ├── IClienteRepository.java         ← CRUD + búsqueda por DNI
        │   │   ├── IProductoRepository.java        ← CRUD de productos
        │   │   ├── IInventarioRepository.java      ← CRUD + consulta de stock
        │   │   ├── IOrdenRepository.java           ← CRUD de órdenes
        │   │   ├── IDetalleOrdenRepository.java    ← CRUD + búsqueda por orden
        │   │   ├── IPagoRepository.java            ← CRUD de pagos
        │   │   ├── IComprobanteRepository.java     ← CRUD + búsqueda por pago/correlativo
        │   │   └── ITicketPostventaRepository.java ← CRUD de tickets
        │   └── service/
        │       ├── CatalogoService.java            ← Lógica: catálogo y disponibilidad
        │       ├── ClienteService.java             ← Lógica: registro y validación RENIEC
        │       ├── OrdenVentaService.java          ← Lógica: creación de órdenes e IGV
        │       ├── PagoFacturacionService.java     ← Lógica: pagos y comprobantes
        │       ├── InventarioDespachoService.java  ← Lógica: despacho y descuento stock
        │       ├── PostventaService.java           ← Lógica: tickets y estado de pedidos
        │       ├── ReniecService.java              ← Integración simulada con RENIEC
        │       └── SunatService.java               ← Integración simulada con SUNAT
        └── resources/
            └── application.properties              ← Configuración de la aplicación
```

---

## 4. Archivos de Configuración

### `pom.xml`
Archivo de configuración de Maven. Define las dependencias del proyecto.

| Dato | Valor |
|------|-------|
| Spring Boot | 3.2.2 |
| Java | 21 |
| Packaging | JAR ejecutable |

**Dependencias principales:**

| Dependencia | Para qué sirve |
|-------------|----------------|
| `spring-boot-starter-web` | Crear endpoints REST (controladores, HTTP) |
| `spring-boot-starter-data-jpa` | ORM: mapear clases Java a tablas de BD |
| `mssql-jdbc` | Conectar con SQL Server |
| `spring-boot-starter-security` | Control de acceso y autenticación (pendiente de activar) |
| `modelmapper:3.2.0` | Convertir automáticamente Entidades ↔ DTOs |

---

### `application.properties`
Configuración de la aplicación en tiempo de ejecución.

```properties
# Conexión a SQL Server con autenticación de Windows (sin usuario/contraseña)
spring.datasource.url=jdbc:sqlserver://LAPTOP-VB6I49QM:1433;databaseName=DB_PhoneCorp;...

# Hibernate actualiza tablas automáticamente al iniciar la app
spring.jpa.hibernate.ddl-auto=update

# Muestra en consola las queries SQL ejecutadas
spring.jpa.show-sql=true

# Usa los nombres de tablas/columnas tal cual están definidos (sin convertir camelCase)
spring.jpa.hibernate.naming.physical-strategy=...PhysicalNamingStrategyStandardImpl
```

---

### `basededatos/createDB.sql`
Script T-SQL para crear la base de datos `DB_PhoneCorp` y sus 8 tablas desde cero. Incluye:
- Claves primarias con `IDENTITY` (autoincremento)
- Claves foráneas entre tablas
- Restricciones `UNIQUE` y `CHECK`
- Valores por defecto con `DEFAULT`

---

## 5. Punto de Entrada

### `PhonecorpBackend.java`
**Paquete:** `com.phonecorp.phonecorpbackend`

Clase principal de la aplicación. Tiene la anotación `@SpringBootApplication` que activa:
- Escaneo automático de todos los componentes (`@Controller`, `@Service`, `@Repository`, `@Configuration`)
- Autoconfiguración de Spring Boot
- Inicialización del contenedor de inyección de dependencias

**Método:** `public static void main(String[] args)` — arranca el servidor embebido Tomcat.

---

## 6. Configuraciones (`config/`)

### `ConexionBD.java`
Implementa el patrón **Singleton** para obtener una conexión JDBC directa a SQL Server. Aunque Spring ya gestiona el pool de conexiones automáticamente con HikariCP, esta clase existe como implementación manual/legacy.

- Constructor privado
- Variable estática `instancia` (única instancia)
- Método `getInstance()` sincronizado (thread-safe)

---

### `CorsConfig.java`
Permite que el frontend (React, Angular, etc.) consuma la API desde otro dominio/puerto sin ser bloqueado por el navegador.

**Configuración actual (desarrollo):**
- Orígenes permitidos: `*` (todos)
- Métodos: GET, POST, PUT, DELETE, OPTIONS, PATCH
- Headers: todos
- No usa credenciales

> **Para producción:** reemplazar `*` por el dominio real del frontend.

---

### `MapperConfig.java`
Registra **ModelMapper** como bean de Spring para convertir automáticamente entidades JPA a DTOs y viceversa, evitando copiar campos manualmente.

- Estrategia `STRICT`: solo mapea campos con nombre exactamente igual (evita errores)
- `skipNullEnabled=true`: si un campo es null en la fuente, no sobreescribe el destino

---

### `RestTemplateConfig.java`
Crea un **cliente HTTP** (`RestTemplate`) listo para hacer llamadas a APIs externas (RENIEC, SUNAT).

- Timeout de conexión: 5 segundos
- Timeout de lectura: 10 segundos

---

### `SecurityConfig.java`
Configura la seguridad HTTP de la aplicación.

**Estado actual (desarrollo):** deja pasar todas las peticiones sin autenticación.

**Configuración aplicada:**
- CSRF deshabilitado (API REST stateless no lo necesita)
- Sesión stateless (sin cookies de sesión, preparado para JWT)
- Todos los endpoints son públicos temporalmente

**Roles planeados para producción:**
```
/api/clientes/**     → ADMIN, VENTAS
/api/ordenes/**      → ADMIN, VENTAS
/api/pagos/**        → FACTURACION
/api/inventario/**   → INVENTARIO
/api/postventa/**    → ADMIN, POSTVENTA
```

---

## 7. Entidades JPA (`domain/`)

Cada entidad es una clase Java anotada con `@Entity` que mapea directamente a una tabla de la base de datos. Hibernate las usa para leer y escribir datos.

### `EntidadCliente` → Tabla `Cliente`

Representa a un cliente registrado en el sistema.

| Campo | Tipo BD | Descripción |
|-------|---------|-------------|
| `idCliente` | INT IDENTITY PK | Identificador único |
| `dniCe` | VARCHAR(20) UNIQUE | DNI o carnet de extranjería |
| `nombresCompletos` | NVARCHAR(150) | Nombre completo |
| `direccion` | NVARCHAR(200) | Dirección de entrega |
| `telefono` | VARCHAR(15) | Opcional |
| `email` | VARCHAR(100) | Opcional |
| `historialCrediticio` | VARCHAR(20) | `BUENO`, `REGULAR`, `MALO` |
| `fechaRegistro` | DATETIME | Fecha automática al crear |

**Relaciones:**
- `@OneToMany` con `EntidadOrdenVenta`
- `@OneToMany` con `EntidadTicketPostventa`

---

### `EntidadProducto` → Tabla `Producto`

Representa un equipo celular del catálogo.

| Campo | Tipo BD | Descripción |
|-------|---------|-------------|
| `idProducto` | INT IDENTITY PK | Identificador único |
| `sku` | VARCHAR(50) UNIQUE | Código de producto único |
| `nombre` | VARCHAR(100) | Nombre comercial |
| `marca` | VARCHAR(50) | Ej: Samsung, Apple |
| `modelo` | VARCHAR(50) | Ej: Galaxy S24 |
| `gama` | VARCHAR(20) | Alta, Media, Baja |
| `precioUnitario` | DECIMAL(10,2) | Precio en soles, >= 0 |
| `descripcion` | VARCHAR(255) | Descripción opcional |
| `fechaRegistro` | DATETIME | Fecha automática |

**Relaciones:**
- `@OneToMany` con `EntidadInventario`
- `@OneToMany` con `EntidadDetalleOrden`

---

### `EntidadInventario` → Tabla `Inventario`

Controla el stock físico de cada producto en almacén.

| Campo | Tipo BD | Descripción |
|-------|---------|-------------|
| `idInventario` | INT IDENTITY PK | Identificador único |
| `producto` | FK → Producto | Producto asociado |
| `stockFisico` | INT | Unidades en almacén, >= 0 |
| `stockComprometido` | INT | Reservado pero no despachado |
| `ubicacionAlmacen` | NVARCHAR(50) | Código de ubicación |
| `fechaActualizacion` | DATETIME | Última actualización |

**Relaciones:**
- `@ManyToOne` con `EntidadProducto` (LAZY)

---

### `EntidadOrdenVenta` → Tabla `OrdenVenta`

Representa una orden de compra creada por un cliente.

| Campo | Tipo BD | Descripción |
|-------|---------|-------------|
| `idOrden` | INT IDENTITY PK | Identificador único |
| `cliente` | FK → Cliente | Cliente que ordenó |
| `fechaEmision` | DATETIME | Fecha automática |
| `estado` | VARCHAR(20) | `PENDIENTE` → `PAGADO` → `DESPACHADO` |
| `modalidadEntrega` | VARCHAR(50) | `Retiro en Tienda` o `Envío a Domicilio` |
| `montoTotal` | DECIMAL(10,2) | Total con IGV y recargo |

**Relaciones:**
- `@ManyToOne` con `EntidadCliente` (LAZY)
- `@OneToMany` con `EntidadDetalleOrden` (CASCADE ALL)
- `@OneToMany` con `EntidadPago`

---

### `EntidadDetalleOrden` → Tabla `DetalleOrden`

Línea de detalle de una orden: qué producto, cuántos y a qué precio.

| Campo | Tipo BD | Descripción |
|-------|---------|-------------|
| `idDetalle` | INT IDENTITY PK | Identificador único |
| `orden` | FK → OrdenVenta | Orden a la que pertenece |
| `producto` | FK → Producto | Producto de esta línea |
| `cantidad` | INT | Unidades, > 0 |
| `precioPactado` | DECIMAL(10,2) | Precio en el momento de la venta |
| `subtotal` | DECIMAL(10,2) | `cantidad * precioPactado` |

**Restricción:** No puede haber dos líneas con el mismo producto en la misma orden (`UNIQUE (id_orden, id_producto)`).

---

### `EntidadPago` → Tabla `Pago`

Registra el pago de una orden de venta.

| Campo | Tipo BD | Descripción |
|-------|---------|-------------|
| `idPago` | INT IDENTITY PK | Identificador único |
| `orden` | FK → OrdenVenta (UNIQUE) | Una orden tiene un solo pago |
| `montoTotal` | DECIMAL(10,2) | Monto cobrado |
| `metodoPago` | VARCHAR(50) | `EFECTIVO`, `TARJETA`, `TRANSFERENCIA` |
| `estadoPago` | VARCHAR(20) | `PENDIENTE`, `COMPLETADO` |
| `fechaPago` | DATETIME | Fecha automática |

**Relaciones:**
- `@ManyToOne` con `EntidadOrdenVenta` (LAZY)
- `@OneToOne` con `EntidadComprobante`

---

### `EntidadComprobante` → Tabla `Comprobante`

Comprobante electrónico (boleta o factura) emitido para un pago.

| Campo | Tipo BD | Descripción |
|-------|---------|-------------|
| `idComprobante` | INT IDENTITY PK | Identificador único |
| `pago` | FK → Pago (UNIQUE) | Un pago tiene un solo comprobante |
| `tipoComprobante` | VARCHAR(20) | `BOLETA` o `FACTURA` |
| `serie` | VARCHAR(10) | `B001` (boleta) o `F001` (factura) |
| `numeroCorrelativo` | VARCHAR(20) | Número de 8 dígitos, ej: `00000042` |
| `hashSunat` | NVARCHAR(255) | UUID simulado como hash SUNAT |
| `fechaEmision` | DATETIME | Fecha de emisión |

**Relaciones:**
- `@OneToOne` con `EntidadPago` (LAZY)

---

### `EntidadTicketPostventa` → Tabla `TicketPostventa`

Registro de solicitudes de soporte postventa (reclamos, consultas, garantías).

| Campo | Tipo BD | Descripción |
|-------|---------|-------------|
| `idTicket` | INT IDENTITY PK | Identificador único |
| `cliente` | FK → Cliente | Cliente que abre el ticket |
| `orden` | FK → OrdenVenta (nullable) | Orden relacionada (opcional) |
| `motivo` | VARCHAR(50) | `RECLAMO`, `CONSULTA`, `GARANTIA` |
| `descripcionCaso` | VARCHAR(500) | Detalle del caso |
| `estadoTicket` | VARCHAR(20) | `ABIERTO`, `EN_PROCESO`, `CERRADO` |
| `fechaRegistro` | DATETIME | Fecha automática |

---

## 8. DTOs (`dto/`)

Los DTOs (Data Transfer Objects) son clases simples que definen qué datos entran y salen de la API. Permiten que la API exponga solo lo necesario sin exponer la estructura interna de las entidades.

### DTOs de Entrada (Request)

| Archivo | Usado en | Campos principales |
|---------|----------|--------------------|
| `ClienteRequestDTO` | Registrar/actualizar cliente | dniCe, nombresCompletos, direccion, telefono, email, historialCrediticio |
| `OrdenVentaRequestDTO` | Crear orden | idCliente, modalidadEntrega, items (lista de ItemOrdenRequest) |
| `ItemOrdenRequest` | Ítem dentro de OrdenVentaRequestDTO | idProducto, cantidad |
| `PagoRequestDTO` | Procesar pago | idOrden, metodoPago, tipoComprobante |
| `TicketPostventaRequestDTO` | Registrar ticket | idCliente, idOrden (opcional), motivo, descripcionCaso |

### DTOs de Salida (Response)

| Archivo | Devuelto por | Campos principales |
|---------|-------------|--------------------|
| `ClienteResponseDTO` | Endpoints de clientes | idCliente, dniCe, nombresCompletos, direccion, telefono, email, historialCrediticio, fechaRegistro |
| `ProductoResponseDTO` | Endpoints de catálogo | idProducto, sku, nombre, marca, modelo, precioUnitario, descripcion, **stockFisico** |
| `OrdenVentaResponseDTO` | Endpoints de órdenes | idOrden, idCliente, nombreCliente, modalidadEntrega, estado, subtotalSinIgv, **igv**, **recargoEnvio**, **montoTotal**, detalles |
| `PagoResponseDTO` | Endpoints de pagos | idPago, idOrden, montoTotal, metodoPago, estadoPago, fechaPago + datos de comprobante |
| `TicketPostventaResponseDTO` | Endpoints de postventa | idTicket, idCliente, nombreCliente, idOrden, motivo, descripcionCaso, estadoTicket, fechaRegistro |
| `ApiErrorDTO` | Todos los errores | timestamp, mensaje, codigo (ej: `ERR_STOCK_01`) |

---

## 9. Excepciones (`exception/`)

### `GlobalExceptionHandler.java`
Clase central anotada con `@RestControllerAdvice`. Captura todas las excepciones lanzadas en cualquier parte de la aplicación y las convierte en respuestas HTTP con formato uniforme (`ApiErrorDTO`).

| Excepción capturada | HTTP Status | Código de error |
|--------------------|-------------|-----------------|
| `StockInsuficienteException` | 409 Conflict | `ERR_STOCK_01` |
| `DniInvalidoException` | 422 Unprocessable Entity | `ERR_RENIEC_01` |
| `HistorialCrediticioBloqueadoException` | 403 Forbidden | `ERR_CREDITO_01` |
| `ResponseStatusException` | Variable | `ERR_HTTP_{status}` |
| `EntityNotFoundException` | 404 Not Found | `ERR_NOT_FOUND_01` |
| `IllegalArgumentException` | 400 Bad Request | `ERR_VALIDACION_01` |
| `Exception` (genérica) | 500 Internal Server Error | `ERR_INTERNO_01` |

---

### `DniInvalidoException.java`
Excepción de negocio lanzada cuando un DNI o CE no pasa la validación de RENIEC. Extiende `RuntimeException`.

**Cuándo se lanza:** en `ClienteService.registrar()`, si `ReniecService.validarIdentidad()` retorna `false`.

---

### `StockInsuficienteException.java`
Excepción de negocio lanzada cuando se intenta ordenar más unidades de las disponibles en inventario. Extiende `RuntimeException`.

**Cuándo se lanza:** en `OrdenVentaService.crearOrden()`, al verificar stock por cada producto del pedido.

---

### `HistorialCrediticioBloqueadoException.java`
Excepción de negocio lanzada cuando un cliente con historial crediticio `MALO` intenta realizar un pago. Extiende `RuntimeException`.

**Cuándo se lanza:** en `PagoFacturacionService.procesarPago()`.

---

## 10. Repositorios (`repository/`)

Cada repositorio extiende `JpaRepository<Entidad, TipoPK>` y hereda automáticamente métodos CRUD: `findAll()`, `findById()`, `save()`, `deleteById()`, etc.

### `IClienteRepository`
```java
Optional<EntidadCliente> buscarPorDniCe(String dniCe)
boolean existePorDniCe(String dniCe)
```

### `IProductoRepository`
Sin métodos adicionales. Usa solo los heredados de JpaRepository.

### `IInventarioRepository`
```java
Optional<Integer> consultarStock(Integer idProducto)
Optional<EntidadInventario> findByProducto_IdProducto(Integer idProducto)
List<EntidadInventario> findByProducto_IdProductoIn(List<Integer> ids)  // evita N+1
```

### `IOrdenRepository`
Sin métodos adicionales.

### `IDetalleOrdenRepository`
```java
List<EntidadDetalleOrden> findByOrden_IdOrden(Integer idOrden)
```

### `IPagoRepository`
Sin métodos adicionales.

### `IComprobanteRepository`
```java
Optional<EntidadComprobante> buscarPorIdPago(Integer idPago)
boolean existePorIdPago(Integer idPago)
Optional<EntidadComprobante> buscarPorCorrelativo(String tipo, String serie, String numero)
boolean existePorCorrelativo(String tipo, String serie, String numero)
```

### `ITicketPostventaRepository`
Sin métodos adicionales.

---

## 11. Servicios (`service/`)

### `CatalogoService.java`
**Responsabilidad:** Exponer el catálogo de productos con información de disponibilidad de stock.

| Método | Descripción |
|--------|-------------|
| `listarTodos()` | Retorna todos los productos con su stockFisico actual. Usa batch query para evitar el problema N+1. |
| `listarPorMarca(String marca)` | Filtra productos por marca (sin distinguir mayúsculas/minúsculas). |
| `obtenerConStock(Integer idProducto)` | Retorna un producto específico. Lanza 409 si el stock es 0. |
| `consultarStock(Integer idProducto)` | Retorna solo el número de unidades disponibles. Retorna 0 si no hay registro de inventario. |

**Inyecta:** `IProductoRepository`, `IInventarioRepository`, `ModelMapper`

---

### `ClienteService.java`
**Responsabilidad:** Gestión del ciclo de vida de clientes con validación de identidad.

| Método | Descripción |
|--------|-------------|
| `listarTodos()` | Retorna todos los clientes mapeados a DTO. |
| `obtenerPorId(Integer id)` | Retorna un cliente o lanza 404. |
| `buscarPorDni(String dniCe)` | Busca por DNI/CE único o lanza 404. |
| `registrar(ClienteRequestDTO)` | Valida campos obligatorios, llama a RENIEC, verifica DNI no duplicado, asigna historial `BUENO` por defecto y guarda. |
| `actualizar(Integer id, ClienteRequestDTO)` | Actualiza solo los campos enviados (no null). No permite cambiar el DNI. |

**Inyecta:** `IClienteRepository`, `ReniecService`, `ModelMapper`

---

### `OrdenVentaService.java`
**Responsabilidad:** Crear órdenes de venta con validación de stock y cálculo automático de totales.

**Constantes de negocio:**
- IGV: **18%**
- Recargo por envío a domicilio: **S/ 15.00**

| Método | Descripción |
|--------|-------------|
| `crearOrden(OrdenVentaRequestDTO)` | Valida cliente e ítems. Por cada producto valida stock. Calcula subtotal, IGV y recargo de envío. Guarda orden y detalles. Usa `@Transactional`. |
| `obtenerOrden(Integer idOrden)` | Retorna la orden con desglose completo (subtotal, IGV, recargo, total). Recalcula montos al consultar. |

**Fórmula de totales:**
```
subtotalSinIgv = Σ (cantidad × precioPactado) por cada ítem
igv            = subtotalSinIgv × 0.18
recargoEnvio   = 15.00 si modalidad = "Envío a Domicilio", sino 0
montoTotal     = subtotalSinIgv + igv + recargoEnvio
```

**Inyecta:** `IOrdenRepository`, `IClienteRepository`, `IProductoRepository`, `IInventarioRepository`, `IDetalleOrdenRepository`

---

### `PagoFacturacionService.java`
**Responsabilidad:** Procesar pagos y emitir comprobantes electrónicos automáticamente.

| Método | Descripción |
|--------|-------------|
| `procesarPago(PagoRequestDTO)` | Verifica que la orden exista y esté en `PENDIENTE`. Bloquea si el cliente tiene historial `MALO`. Crea el pago, llama a SUNAT para obtener serie/número/hash, crea el comprobante y actualiza la orden a `PAGADO`. Todo en una `@Transactional`. |
| `obtenerComprobante(Integer idPago)` | Retorna los datos del pago y su comprobante asociado. |

**Inyecta:** `IOrdenRepository`, `IPagoRepository`, `IComprobanteRepository`, `SunatService`

---

### `InventarioDespachoService.java`
**Responsabilidad:** Gestionar el despacho de órdenes pagadas y actualizar el inventario.

| Método | Descripción |
|--------|-------------|
| `despacharOrden(Integer idOrden)` | Verifica que la orden esté en `PAGADO`. Por cada producto del detalle, descuenta del stock físico (falla si stock insuficiente). Actualiza la orden a `DESPACHADO` y genera un documento de salida. Usa `@Transactional`. |
| `consultarStock(Integer idProducto)` | Retorna `{idProducto, stockFisico, disponible}`. |

**Documentos generados automáticamente:**
- `GUIA_REMISION` — si la modalidad es "Envío a Domicilio"
- `ACTA_ENTREGA` — si es "Retiro en Tienda"

**Inyecta:** `IOrdenRepository`, `IDetalleOrdenRepository`, `IInventarioRepository`

---

### `PostventaService.java`
**Responsabilidad:** Registrar tickets de soporte postventa y consultar el estado de pedidos.

| Método | Descripción |
|--------|-------------|
| `registrarTicket(TicketPostventaRequestDTO)` | Valida cliente y motivo. Asocia la orden si se proporciona `idOrden`. Crea el ticket con estado `ABIERTO`. |
| `obtenerTicket(Integer idTicket)` | Retorna el ticket o lanza 404. |
| `consultarEstadoPedido(Integer idOrden)` | Retorna `{idOrden, estado, modalidadEntrega, fechaEmision, montoTotal}` para que el cliente consulte su pedido. |

**Inyecta:** `IClienteRepository`, `IOrdenRepository`, `ITicketPostventaRepository`

---

### `ReniecService.java`
**Responsabilidad:** Validar la identidad de clientes contra el registro RENIEC.

**Estado actual (mock/simulado):**

| Método | Lógica actual |
|--------|---------------|
| `validarIdentidad(String dniCe)` | Retorna `false` si el DNI termina en `0` o `9`. En producción haría una llamada HTTP a la API real de RENIEC. |
| `consultarNombre(String dniCe)` | Retorna `"Persona Registrada (MOCK) - DNI {dniCe}"` si es válido. |

**Para producción:** usar `RestTemplate` inyectado para hacer la llamada HTTP a la API oficial de RENIEC.

---

### `SunatService.java`
**Responsabilidad:** Emitir comprobantes electrónicos a través de SUNAT/OSE.

**Estado actual (mock/simulado):**

| Método | Lógica actual |
|--------|---------------|
| `emitirComprobante(String tipo, BigDecimal monto)` | Genera serie (`B001` o `F001`), número correlativo aleatorio de 8 dígitos y un UUID como hash SUNAT. Retorna `String[]{serie, numeroCorrelativo, hash}`. |

**Para producción:** usar `RestTemplate` para llamar al OSE (Operador de Servicios Electrónicos) autorizado por SUNAT.

---

## 12. Controladores (`controller/`)

Todos los controladores usan `@RestController` y delegan **toda** la lógica al servicio correspondiente. No contienen validaciones de negocio.

### `CatalogoController.java`
**Ruta base:** `/api/catalogo`

| Método HTTP | Ruta | Descripción | Status OK |
|-------------|------|-------------|-----------|
| `GET` | `/api/catalogo/productos` | Lista todos los productos con stock actual | 200 |
| `GET` | `/api/catalogo/productos/marca/{marca}` | Filtra productos por marca | 200 |
| `GET` | `/api/catalogo/productos/{idProducto}` | Obtiene un producto por ID (valida que tenga stock) | 200 |
| `GET` | `/api/catalogo/productos/{idProducto}/stock` | Retorna solo el número de unidades disponibles | 200 |

---

### `ClienteController.java`
**Ruta base:** `/api/clientes`

| Método HTTP | Ruta | Descripción | Body entrada | Status OK |
|-------------|------|-------------|-------------|-----------|
| `GET` | `/api/clientes` | Lista todos los clientes | — | 200 |
| `GET` | `/api/clientes/{idCliente}` | Obtiene un cliente por ID | — | 200 |
| `GET` | `/api/clientes/buscar?dni=...` | Busca un cliente por DNI/CE | — | 200 |
| `POST` | `/api/clientes/registrar` | Registra un nuevo cliente con validación RENIEC | `ClienteRequestDTO` | 201 |
| `PUT` | `/api/clientes/{idCliente}` | Actualiza datos de un cliente existente | `ClienteRequestDTO` | 200 |

---

### `OrdenVentaController.java`
**Ruta base:** `/api/ordenes`

| Método HTTP | Ruta | Descripción | Body entrada | Status OK |
|-------------|------|-------------|-------------|-----------|
| `POST` | `/api/ordenes/crear` | Crea una orden de venta con desglose de montos | `OrdenVentaRequestDTO` | 201 |
| `GET` | `/api/ordenes/{idOrden}` | Obtiene una orden con su desglose completo | — | 200 |

---

### `PagoFacturacionController.java`
**Ruta base:** `/api/pagos`

| Método HTTP | Ruta | Descripción | Body entrada | Status OK |
|-------------|------|-------------|-------------|-----------|
| `POST` | `/api/pagos/procesar` | Procesa el pago y emite el comprobante automáticamente | `PagoRequestDTO` | 201 |
| `GET` | `/api/pagos/comprobante/{idPago}` | Obtiene los datos del pago y su comprobante | — | 200 |

---

### `ComprobanteController.java`
**Ruta base:** `/api/comprobantes`

| Método HTTP | Ruta | Descripción | Status OK |
|-------------|------|-------------|-----------|
| `GET` | `/api/comprobantes` | Lista todos los comprobantes | 200 |
| `GET` | `/api/comprobantes/{idComprobante}` | Obtiene un comprobante por ID | 200 |
| `GET` | `/api/comprobantes/pago/{idPago}` | Obtiene el comprobante de un pago específico | 200 |
| `POST` | `/api/comprobantes?idPago=...` | Crea un comprobante manualmente (uso administrativo) | `EntidadComprobante` | 201 |

> **Nota:** La creación normal de comprobantes ocurre automáticamente al pagar en `/api/pagos/procesar`. Este endpoint es de uso administrativo/correccional.

---

### `InventarioDespachoController.java`
**Ruta base:** `/api/inventario`

| Método HTTP | Ruta | Descripción | Status OK |
|-------------|------|-------------|-----------|
| `POST` | `/api/inventario/despachar/{idOrden}` | Despacha una orden pagada y descuenta stock | 200 |
| `GET` | `/api/inventario/stock/{idProducto}` | Consulta el stock de un producto | 200 |

---

### `PostventaController.java`
**Ruta base:** `/api/postventa`

| Método HTTP | Ruta | Descripción | Body entrada | Status OK |
|-------------|------|-------------|-------------|-----------|
| `POST` | `/api/postventa/tickets` | Registra un nuevo ticket de soporte | `TicketPostventaRequestDTO` | 201 |
| `GET` | `/api/postventa/tickets/{idTicket}` | Obtiene un ticket por ID | — | 200 |
| `GET` | `/api/postventa/pedido/{idOrden}/estado` | Consulta el estado de un pedido (para el cliente) | — | 200 |

---

## 13. Flujo Completo de Negocio

El flujo estándar de una venta completa es el siguiente:

```
PASO 1 — Registrar cliente
POST /api/clientes/registrar
  ↓ Valida DNI con RENIEC
  ↓ Guarda en BD
  ← Retorna idCliente

PASO 2 — Ver catálogo
GET /api/catalogo/productos
  ↓ Lista productos con stock disponible
  ← Retorna lista de productos

PASO 3 — Crear orden de venta
POST /api/ordenes/crear
  ↓ Valida stock por cada producto
  ↓ Calcula subtotal + IGV 18% + recargo envío S/15
  ↓ Estado: PENDIENTE
  ← Retorna idOrden, montoTotal con desglose

PASO 4 — Procesar pago
POST /api/pagos/procesar
  ↓ Valida historial crediticio del cliente (MALO → 403)
  ↓ Valida que la orden esté PENDIENTE (ya pagada → 409)
  ↓ Crea registro de pago
  ↓ Emite comprobante (BOLETA o FACTURA via SunatService)
  ↓ Estado orden: PAGADO
  ← Retorna datos del pago + comprobante (serie, número, hash)

PASO 5 — Despachar orden
POST /api/inventario/despachar/{idOrden}
  ↓ Valida que la orden esté PAGADA
  ↓ Descuenta stock físico de cada producto
  ↓ Genera documento (GUIA_REMISION o ACTA_ENTREGA)
  ↓ Estado orden: DESPACHADO
  ← Retorna datos del despacho y documento generado

EN CUALQUIER MOMENTO:
GET /api/postventa/pedido/{idOrden}/estado
  ← Retorna estado actual: PENDIENTE / PAGADO / DESPACHADO

POSTVENTA (si hay problemas):
POST /api/postventa/tickets
  ↓ Registra reclamo, consulta o garantía
  ← Retorna idTicket con estado ABIERTO
```

---

## 14. Resumen de Todos los Endpoints (23 en total)

| # | Módulo | Método | Ruta | Descripción |
|---|--------|--------|------|-------------|
| 1 | Clientes | GET | `/api/clientes` | Listar todos los clientes |
| 2 | Clientes | GET | `/api/clientes/{id}` | Obtener cliente por ID |
| 3 | Clientes | GET | `/api/clientes/buscar?dni=` | Buscar por DNI/CE |
| 4 | Clientes | POST | `/api/clientes/registrar` | Registrar cliente (con RENIEC) |
| 5 | Clientes | PUT | `/api/clientes/{id}` | Actualizar cliente |
| 6 | Catálogo | GET | `/api/catalogo/productos` | Listar productos con stock |
| 7 | Catálogo | GET | `/api/catalogo/productos/marca/{marca}` | Filtrar por marca |
| 8 | Catálogo | GET | `/api/catalogo/productos/{id}` | Obtener producto por ID |
| 9 | Catálogo | GET | `/api/catalogo/productos/{id}/stock` | Consultar stock de un producto |
| 10 | Órdenes | POST | `/api/ordenes/crear` | Crear orden de venta |
| 11 | Órdenes | GET | `/api/ordenes/{id}` | Obtener orden con desglose |
| 12 | Pagos | POST | `/api/pagos/procesar` | Procesar pago + emitir comprobante |
| 13 | Pagos | GET | `/api/pagos/comprobante/{idPago}` | Obtener comprobante de un pago |
| 14 | Comprobantes | GET | `/api/comprobantes` | Listar todos los comprobantes |
| 15 | Comprobantes | GET | `/api/comprobantes/{id}` | Obtener comprobante por ID |
| 16 | Comprobantes | GET | `/api/comprobantes/pago/{idPago}` | Obtener comprobante por pago |
| 17 | Comprobantes | POST | `/api/comprobantes` | Crear comprobante manualmente |
| 18 | Inventario | POST | `/api/inventario/despachar/{idOrden}` | Despachar orden y descontar stock |
| 19 | Inventario | GET | `/api/inventario/stock/{idProducto}` | Consultar stock de producto |
| 20 | Postventa | POST | `/api/postventa/tickets` | Registrar ticket de soporte |
| 21 | Postventa | GET | `/api/postventa/tickets/{id}` | Obtener ticket por ID |
| 22 | Postventa | GET | `/api/postventa/pedido/{idOrden}/estado` | Consultar estado de pedido |

---

## 15. Diagrama de Relaciones entre Entidades

```
Cliente ──────────────────────────────────────────────────┐
  │                                                        │
  │ 1:N                                                    │ 1:N
  ▼                                                        ▼
OrdenVenta ──── 1:N ──→ DetalleOrden ──── N:1 ──→ Producto
  │                                                   │
  │ 1:1                                               │ 1:1
  ▼                                                   ▼
Pago ──── 1:1 ──→ Comprobante              Inventario
```

```
Cliente ──── 1:N ──→ TicketPostventa ◄── N:1 (opcional) ── OrdenVenta
```

---

## 16. Tecnologías y Decisiones Técnicas

| Tecnología / Decisión | Motivo |
|-----------------------|--------|
| **BigDecimal** para montos | Precisión exacta en operaciones monetarias (evita errores de punto flotante) |
| **FetchType.LAZY** en todas las relaciones | Evita cargar datos innecesarios, mejora el rendimiento |
| **@JsonIgnore** en relaciones bidireccionales | Previene referencias circulares al serializar a JSON |
| **@Transactional** en operaciones críticas | Garantiza atomicidad: si algo falla, todo se revierte |
| **RoundingMode.HALF_UP** | Redondeo contable estándar (similar a "redondeo comercial") |
| **Batch query en inventario** | Resuelve el problema N+1: en vez de una query por producto, hace una sola query para todos |
| **Patrón DTO** | La API no expone la estructura interna de las entidades, más seguro y flexible |
| **GlobalExceptionHandler** | Todos los errores tienen el mismo formato de respuesta (`ApiErrorDTO`) |
| **ModelMapper STRICT** | Evita mapeos ambiguos entre campos de nombres similares |

---

## 17. Pendientes para Producción

- [ ] Activar roles en `SecurityConfig` e implementar JWT
- [ ] Reemplazar `ReniecService` mock por llamada HTTP real a RENIEC
- [ ] Reemplazar `SunatService` mock por llamada HTTP real a OSE autorizado
- [ ] Cambiar CORS de `*` al dominio real del frontend
- [ ] Cambiar `ddl-auto=update` a `validate` (no modificar BD en producción)
- [ ] Deshabilitar `show-sql=true` en producción
- [ ] Configurar variables de entorno para credenciales de BD
- [ ] Agregar paginación en endpoints que listan colecciones
- [ ] Agregar logging estructurado
