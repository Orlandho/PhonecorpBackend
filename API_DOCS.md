# PhoneCorp Backend - Documentacion para Frontend

## Informacion General

- URL base: `http://localhost:8080`
- Formato de datos: JSON (Content-Type: application/json)
- CORS: habilitado para cualquier origen
- Autenticacion: ninguna por ahora (todos los endpoints son publicos)

---

## Despliegue en Servidor Ubuntu 24.04

### Requisitos del servidor
- Java 21 instalado: `sudo apt install -y openjdk-21-jre-headless`
- SQL Server accesible desde el servidor (puede ser otra maquina en la misma red)

### Paso 1: Copiar el JAR al servidor
```bash
scp target/PhonecorpBackend-1.0-SNAPSHOT.jar usuario@IP_SERVIDOR:/opt/phonecorp/
```

### Paso 2: Configurar las variables de entorno en el servidor
```bash
export DB_URL=jdbc:sqlserver://IP_SQL_SERVER:1433;databaseName=DB_PhoneCorp;encrypt=true;trustServerCertificate=true
export DB_USERNAME=sa
export DB_PASSWORD=tuPasswordSegura
```

### Paso 3: Ejecutar el backend con el perfil de produccion
```bash
java -jar -Dspring.profiles.active=prod /opt/phonecorp/PhonecorpBackend-1.0-SNAPSHOT.jar
```

### Paso 4 (opcional): Ejecutar como servicio para que arranque automaticamente
Crea el archivo `/etc/systemd/system/phonecorp.service`:
```ini
[Unit]
Description=PhoneCorp Backend
After=network.target

[Service]
User=ubuntu
WorkingDirectory=/opt/phonecorp
Environment="DB_URL=jdbc:sqlserver://IP_SQL_SERVER:1433;databaseName=DB_PhoneCorp;encrypt=true;trustServerCertificate=true"
Environment="DB_USERNAME=sa"
Environment="DB_PASSWORD=tuPasswordSegura"
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod /opt/phonecorp/PhonecorpBackend-1.0-SNAPSHOT.jar
SuccessExitStatus=143
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

Luego activa el servicio:
```bash
sudo systemctl daemon-reload
sudo systemctl enable phonecorp
sudo systemctl start phonecorp
sudo systemctl status phonecorp
```

Ver logs en tiempo real:
```bash
sudo journalctl -u phonecorp -f
```

### Nota sobre el firewall
Si el puerto 8080 no es accesible desde fuera, abrelo:
```bash
sudo ufw allow 8080
```

---

## Formato de Errores

Cuando una peticion falla, el backend responde con este formato:

```json
{
  "timestamp": "2026-02-22T21:14:16",
  "mensaje": "Descripcion del error",
  "codigo": 404
}
```

Codigos HTTP posibles:
- 200 OK - consulta exitosa
- 201 Created - recurso creado exitosamente
- 400 Bad Request - datos de entrada invalidos
- 403 Forbidden - pago bloqueado por historial crediticio MALO
- 404 Not Found - recurso no encontrado
- 409 Conflict - conflicto de datos (ej: DNI ya registrado, orden ya pagada)

---

## Flujo de Negocio Recomendado

Para completar una venta de principio a fin, los endpoints deben llamarse en este orden:

1. Registrar o buscar el cliente (Clientes)
2. Ver productos disponibles (Catalogo)
3. Crear la orden de venta (Ordenes)
4. Procesar el pago (Pagos) -- esto emite el comprobante automaticamente
5. Despachar la orden (Inventario) -- esto descuenta el stock

---

## Modulo Clientes

### Listar todos los clientes

```
GET /api/clientes
```

Respuesta 200:
```json
[
  {
    "idCliente": 1,
    "dniCe": "12345678",
    "nombresCompletos": "Juan Perez",
    "direccion": "Av. Lima 123",
    "telefono": "999888777",
    "email": "juan@email.com",
    "historialCrediticio": "BUENO",
    "fechaRegistro": "2026-02-22T21:14:16"
  }
]
```

---

### Obtener un cliente por ID

```
GET /api/clientes/{idCliente}
```

Respuesta 200: mismo objeto de arriba (sin el array).

Respuesta 404:
```json
{ "mensaje": "Cliente no encontrado con ID: 5", "codigo": 404 }
```

---

### Buscar cliente por DNI o CE

```
GET /api/clientes/buscar?dni=12345678
```

Respuesta 200: mismo objeto cliente.

---

### Registrar un cliente

```
POST /api/clientes/registrar
Content-Type: application/json
```

Body:
```json
{
  "dniCe": "12345678",
  "nombresCompletos": "Juan Perez",
  "direccion": "Av. Lima 123",
  "telefono": "999888777",
  "email": "juan@email.com",
  "historialCrediticio": "BUENO"
}
```

Campos obligatorios: `dniCe`, `nombresCompletos`, `direccion`
Campos opcionales: `telefono`, `email`, `historialCrediticio` (default: "BUENO")

Valores validos para `historialCrediticio`: `"BUENO"`, `"REGULAR"`, `"MALO"`

Respuesta 201: objeto cliente completo con el `idCliente` asignado.

Errores posibles:
- 400: dniCe, nombresCompletos o direccion vacios
- 400: DNI invalido segun validacion RENIEC (DNIs que terminan en 0 o 9 son rechazados en el entorno de pruebas)
- 409: ya existe un cliente con ese DNI/CE

---

### Actualizar un cliente

```
PUT /api/clientes/{idCliente}
Content-Type: application/json
```

Body (todos los campos son opcionales, solo se actualizan los que se envian):
```json
{
  "nombresCompletos": "Juan Carlos Perez",
  "direccion": "Av. Arequipa 999",
  "telefono": "988776655",
  "email": "juancarlos@email.com",
  "historialCrediticio": "REGULAR"
}
```

Respuesta 200: objeto cliente actualizado.

Nota: el `dniCe` no se puede cambiar por este endpoint.

---

## Modulo Catalogo

### Listar todos los productos

```
GET /api/catalogo/productos
```

Respuesta 200:
```json
[
  {
    "idProducto": 1,
    "sku": "SAM-A55-128",
    "nombre": "Samsung Galaxy A55",
    "marca": "Samsung",
    "modelo": "Galaxy A55",
    "precioUnitario": 1299.90,
    "descripcion": "Smartphone 6.6 pulgadas 128GB",
    "stockFisico": 50
  }
]
```

---

### Listar productos por marca

```
GET /api/catalogo/productos/marca/{marca}
```

Ejemplo: `GET /api/catalogo/productos/marca/Samsung`

Respuesta 200: array de productos de esa marca.

---

### Obtener un producto por ID

```
GET /api/catalogo/productos/{idProducto}
```

Respuesta 200: objeto producto con `stockFisico`.

Errores posibles:
- 404: producto no encontrado
- 409: el producto existe pero no tiene stock disponible (stockFisico = 0)

---

### Consultar stock de un producto

```
GET /api/catalogo/productos/{idProducto}/stock
```

Respuesta 200: numero entero con el stock disponible.

Ejemplo de respuesta: `50`

---

## Modulo Ordenes de Venta

### Crear una orden de venta

```
POST /api/ordenes/crear
Content-Type: application/json
```

Body:
```json
{
  "idCliente": 1,
  "modalidadEntrega": "Retiro en Tienda",
  "items": [
    { "idProducto": 1, "cantidad": 2 },
    { "idProducto": 3, "cantidad": 1 }
  ]
}
```

Valores validos para `modalidadEntrega`:
- `"Retiro en Tienda"` - sin recargo adicional
- `"Envio a Domicilio"` - agrega S/ 15.00 al total automaticamente

Respuesta 201:
```json
{
  "idOrden": 1,
  "idCliente": 1,
  "nombreCliente": "Juan Perez",
  "modalidadEntrega": "Retiro en Tienda",
  "estado": "PENDIENTE",
  "fechaEmision": "2026-02-22T21:30:00",
  "subtotalSinIgv": 2599.80,
  "igv": 467.96,
  "recargoEnvio": 0.00,
  "montoTotal": 3067.76,
  "detalles": [
    {
      "idDetalle": 1,
      "idProducto": 1,
      "nombreProducto": "Samsung Galaxy A55",
      "cantidad": 2,
      "precioPactado": 1299.90,
      "subtotal": 2599.80
    }
  ]
}
```

Nota sobre los calculos:
- `subtotalSinIgv` = suma de (precio * cantidad) de todos los items
- `igv` = subtotalSinIgv * 18%
- `recargoEnvio` = S/ 15.00 si modalidad es "Envio a Domicilio", sino 0.00
- `montoTotal` = subtotalSinIgv + igv + recargoEnvio

Errores posibles:
- 404: cliente no encontrado
- 404: algun producto del listado no existe
- 400: la lista de items esta vacia
- 400: stock insuficiente para algun producto (incluye cuanto hay disponible vs cuanto se pide)

---

### Obtener una orden por ID

```
GET /api/ordenes/{idOrden}
```

Respuesta 200: mismo objeto de arriba.

---

## Modulo Pagos y Comprobantes

### Procesar un pago

Este endpoint paga la orden Y emite el comprobante electronico (simulado con SUNAT) en una sola operacion.

```
POST /api/pagos/procesar
Content-Type: application/json
```

Body:
```json
{
  "idOrden": 1,
  "metodoPago": "TARJETA",
  "tipoComprobante": "BOLETA"
}
```

Valores validos para `metodoPago`: `"EFECTIVO"`, `"TARJETA"`, `"TRANSFERENCIA"`
Valores validos para `tipoComprobante`: `"BOLETA"`, `"FACTURA"`

Respuesta 201:
```json
{
  "idPago": 1,
  "idOrden": 1,
  "montoTotal": 3067.76,
  "metodoPago": "TARJETA",
  "estadoPago": "COMPLETADO",
  "fechaPago": "2026-02-22T21:35:00",
  "idComprobante": 1,
  "tipoComprobante": "BOLETA",
  "serie": "B001",
  "numeroCorrelativo": "00000042",
  "hashSunat": "a3f9c2d1...",
  "fechaEmisionComprobante": "2026-02-22T21:35:00"
}
```

Errores posibles:
- 403: el cliente tiene historialCrediticio = "MALO" (pago bloqueado)
- 404: orden no encontrada
- 409: la orden ya fue pagada anteriormente

Efecto en la orden: el estado de la orden cambia de `PENDIENTE` a `PAGADO`.

---

### Obtener comprobante por ID de pago

```
GET /api/pagos/comprobante/{idPago}
```

Respuesta 200: mismo objeto de respuesta del procesamiento de pago.

---

## Modulo Comprobantes (consulta directa)

### Listar todos los comprobantes

```
GET /api/comprobantes
```

---

### Obtener un comprobante por ID

```
GET /api/comprobantes/{idComprobante}
```

---

### Obtener comprobante por ID de pago

```
GET /api/comprobantes/pago/{idPago}
```

---

## Modulo Inventario y Despacho

### Despachar una orden

Reduce el stock fisico de cada producto en la orden y genera la documentacion de salida.

```
POST /api/inventario/despachar/{idOrden}
```

No requiere body.

Respuesta 200:
```json
{
  "mensaje": "Despacho realizado correctamente.",
  "idOrden": 1,
  "tipoDocumento": "ACTA_ENTREGA",
  "numeroDocumento": "DOC-1-83421",
  "fechaDespacho": "2026-02-22T21:40:00",
  "productosActualizados": 2
}
```

Valores posibles para `tipoDocumento`:
- `"GUIA_REMISION"` si la modalidad de la orden fue "Envio a Domicilio"
- `"ACTA_ENTREGA"` si fue "Retiro en Tienda"

Errores posibles:
- 404: orden no encontrada
- 409: la orden no esta en estado PAGADO (no se puede despachar una orden PENDIENTE o ya DESPACHADA)
- 409: stock insuficiente al momento de despachar

Efecto: el estado de la orden cambia de `PAGADO` a `DESPACHADO`.

---

### Consultar stock de un producto

```
GET /api/inventario/stock/{idProducto}
```

Respuesta 200:
```json
{
  "idProducto": 1,
  "stockFisico": 48,
  "disponible": true
}
```

---

## Modulo Postventa

### Registrar un ticket de reclamo o consulta

```
POST /api/postventa/tickets
Content-Type: application/json
```

Body:
```json
{
  "idCliente": 1,
  "idOrden": 1,
  "motivo": "RECLAMO",
  "descripcionCaso": "El telefono llego con la pantalla rayada"
}
```

Campos obligatorios: `idCliente`, `motivo`
Campos opcionales: `idOrden` (puede ser null si es una consulta general sin orden asociada), `descripcionCaso`

Valores sugeridos para `motivo`: `"RECLAMO"`, `"CONSULTA"`, `"GARANTIA"`

Respuesta 201:
```json
{
  "idTicket": 1,
  "idCliente": 1,
  "nombreCliente": "Juan Perez",
  "idOrden": 1,
  "motivo": "RECLAMO",
  "descripcionCaso": "El telefono llego con la pantalla rayada",
  "estadoTicket": "ABIERTO",
  "fechaRegistro": "2026-02-22T21:45:00"
}
```

Errores posibles:
- 400: motivo vacio
- 404: cliente no encontrado
- 404: orden no encontrada (si se envio idOrden)

---

### Obtener un ticket por ID

```
GET /api/postventa/tickets/{idTicket}
```

Respuesta 200: mismo objeto ticket.

---

### Consultar el estado de un pedido

Permite al cliente ver en que etapa esta su orden usando solo el ID de la orden.

```
GET /api/postventa/pedido/{idOrden}/estado
```

Respuesta 200:
```json
{
  "idOrden": 1,
  "estado": "DESPACHADO",
  "modalidadEntrega": "Retiro en Tienda",
  "fechaEmision": "2026-02-22T21:30:00",
  "montoTotal": 3067.76
}
```

Valores posibles para `estado`:
- `"PENDIENTE"` - orden creada, esperando pago
- `"PAGADO"` - pago procesado, esperando despacho
- `"DESPACHADO"` - orden entregada o enviada

---

## Resumen de Todos los Endpoints

### Clientes (/api/clientes)
- GET    /api/clientes                     - Listar todos
- GET    /api/clientes/{id}                - Obtener por ID
- GET    /api/clientes/buscar?dni={dni}    - Buscar por DNI/CE
- POST   /api/clientes/registrar           - Registrar nuevo cliente
- PUT    /api/clientes/{id}                - Actualizar cliente

### Catalogo (/api/catalogo)
- GET    /api/catalogo/productos                      - Listar todos los productos
- GET    /api/catalogo/productos/marca/{marca}        - Filtrar por marca
- GET    /api/catalogo/productos/{id}                 - Obtener producto con stock
- GET    /api/catalogo/productos/{id}/stock           - Consultar solo el stock

### Ordenes (/api/ordenes)
- POST   /api/ordenes/crear        - Crear orden de venta
- GET    /api/ordenes/{id}         - Obtener orden por ID

### Pagos (/api/pagos)
- POST   /api/pagos/procesar                  - Procesar pago y emitir comprobante
- GET    /api/pagos/comprobante/{idPago}       - Obtener comprobante por ID de pago

### Comprobantes (/api/comprobantes)
- GET    /api/comprobantes                    - Listar todos
- GET    /api/comprobantes/{id}               - Obtener por ID
- GET    /api/comprobantes/pago/{idPago}      - Obtener por ID de pago

### Inventario (/api/inventario)
- POST   /api/inventario/despachar/{idOrden}   - Despachar orden
- GET    /api/inventario/stock/{idProducto}    - Consultar stock

### Postventa (/api/postventa)
- POST   /api/postventa/tickets                        - Registrar ticket
- GET    /api/postventa/tickets/{id}                   - Obtener ticket
- GET    /api/postventa/pedido/{idOrden}/estado         - Consultar estado del pedido
