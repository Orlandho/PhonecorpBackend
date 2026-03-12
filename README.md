# PhoneCorp Backend API

## Descripción del Proyecto
Este repositorio contiene el código fuente del backend para PhoneCorp, una empresa dedicada a la venta minorista de equipos móviles tecnológicos. El sistema expone una API REST segura y escalable diseñada para gestionar el ciclo de vida completo de las ventas de smartphones, integrando el flujo desde la atención al cliente hasta la entrega final del producto.

## Arquitectura y Tecnologías
El sistema está construido bajo la arquitectura de capas Frontera Control Entidad y utiliza tecnologías robustas de desarrollo empresarial:
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.2
* **Persistencia:** Spring Data JPA con capa de abstracción Hibernate
* **Base de Datos:** Microsoft SQL Server 2022
* **Integración:** Arquitectura orientada a servicios RESTful con configuración CORS habilitada

## Módulos Principales
La solución web automatiza los siguientes procesos clave del negocio:
* **Gestión de Clientes:** Registro y validación biométrica de identidad conectada directamente con los servicios de RENIEC.
* **Catálogo e Inventario:** Consulta de disponibilidad de equipos móviles y control del Kardex local.
* **Órdenes de Venta:** Generación de pedidos estructurados para modalidades de recojo en tienda o envío a domicilio.
* **Facturación y Pagos:** Procesamiento de cobros y emisión de comprobantes de pago electrónicos validados por organismos oficiales como SUNAT.
* **Postventa:** Sistema de tickets para gestionar garantías y soporte técnico.

## Requisitos de Entorno
Para desplegar este proyecto en un entorno local o servidor de producción, se requiere lo siguiente:
* Java Development Kit 21
* Apache Maven
* Instancia activa de Microsoft SQL Server 2022

## Configuración y Ejecución
1. Clonar el repositorio en la máquina destino.
2. Configurar las credenciales de conexión a la base de datos mediante las variables de entorno DB_URL, DB_USERNAME y DB_PASSWORD.
3. Compilar el código fuente utilizando Maven para generar el archivo ejecutable.
4. Levantar el servicio especificando el perfil de producción correspondiente. La base de datos y sus tablas se generarán de forma automática al iniciar la aplicación.
