# 🛒 Ecosistema Extendido de Microservicios: Perfulandia Enterprise

### 👥 Integrantes del Equipo
- **Jhon Alfaro** 
- **Esteban Fernández**

---

## 📄 Descripción del Contexto y Dominio
Perfulandia es una plataforma distribuida corporativa diseñada bajo una arquitectura de microservicios para la gestión automatizada, venta B2B/B2C, soporte preventa/postventa y logística internacional de productos de perfumería fina. El ecosistema resuelve la consistencia de datos contables mediante un patrón distributivo de bases de datos aisladas (*Database-per-Service*), garantizando interoperabilidad transaccional sincrónica, orquestación centralizada de rutas y validaciones algorítmicas de negocio.

---

## 🗂️ Matriz Completa de Microservicios e Infraestructura

| Componente | Puerto Local | Tecnología Core | Propósito Técnico en el Dominio |
| :--- | :--- | :--- | :--- |
| **`eureka-server`** | `8761` | Spring Cloud Netflix | Servidor de descubrimiento de instancias y alta disponibilidad. |
| **`api-gateway`** | `8080` | Spring Cloud Gateway | Enrutador perimetral centralizado y balanceador de carga. |
| **`microservicio-usuarios`** | `8090` | Spring Boot + JPA | Gestión de identidades con validación Módulo 11 (RUT Chileno). |
| **`microservicio-productos`** | `8081` | Spring Boot + JPA | Catálogo maestro de fragancias y inicialización de datos. |
| **`microservicio-pedidos`** | `8082` | Spring Boot + OpenFeign | Core transaccional; orquesta flujos entre Stock y Pagos. |
| **`microservicio-stock`** | `8083` | Spring Boot + H2 | Control reactivo y reserva temporal de existencias en bodega. |
| **`microservicio-pagos`** | `8084` | Spring Boot + JPA | Pasarela financiera y procesamiento seguro de transacciones. |
| **`microservicio_notificaciones`** | `8089` | Spring Boot | Despacho asíncrono de alertas de compra mediante DTOs. |
| **`microservicio-envios`** | `8091` | Spring Boot + JPA | Logística de despachos, tracking y asignación de transportes. |
| **`microservicio-proveedores`** | `8085` | Spring Boot + JPA | Gestión de cadena de suministro y órdenes de reabastecimiento. |
| **`microservicio-soporte`** | `8086` | Spring Boot + JPA | Centralización de Tickets de incidencias y Postventa. |

---

## 🛣️ Enrutamiento Perimetral en API Gateway (Puerto: 8080)
El **API Gateway** centraliza las peticiones mediante filtros y predicados dinámicos declarados en su arquitectura YAML:

* **Usuarios:** `GET/POST` ➡️ `http://localhost:8080/api/usuarios/**`
* **Productos:** `GET/POST` ➡️ `http://localhost:8080/api/productos/**`
* **Pedidos:** `GET/POST` ➡️ `http://localhost:8080/api/pedidos/**`
* **Stock:** `GET/PUT` ➡️ `http://localhost:8080/api/stock/**`
* **Pagos:** `POST` ➡️ `http://localhost:8080/api/pagos/**`
* **Notificaciones:** `POST` ➡️ `http://localhost:8080/api/notificaciones/**`
* **Envíos:** `GET/POST` ➡️ `http://localhost:8080/api/envios/**`
* **Proveedores:** `GET/POST` ➡️ `http://localhost:8080/api/proveedores/**`
* **Soporte:** `GET/POST/PUT` ➡️ `http://localhost:8080/api/soporte/**`

---

## 🔌 Documentación Técnica Swagger / OpenAPI (UI)
Cada microservicio expone de manera independiente su documentación de endpoints en los siguientes enlaces locales:

* 👥 **Usuarios UI:** `http://localhost:8090/swagger-ui/index.html`
* 🛍️ **Productos UI:** `http://localhost:8081/swagger-ui/index.html`
* 🛒 **Pedidos UI:** `http://localhost:8082/swagger-ui/index.html`
* 💳 **Pagos UI:** `http://localhost:8084/swagger-ui/index.html`
* 🚚 **Envíos UI:** `http://localhost:8091/swagger-ui/index.html`
* 🛠️ **Soporte UI:** `http://localhost:8086/swagger-ui/index.html`

---

## 🛠️ Instrucciones de Ejecución Local
1. **Paso 1:** Compilar el proyecto padre y descargar dependencias Maven:
   ```bash
   ./mvnw clean install -DskipTests
2. Paso 2: Levantar obligatoriamente la infraestructura en este orden:
Ejecutar eureka-server (Puerto 8761).
Ejecutar api-gateway (Puerto 8080).
3. Paso 3: Levantar los microservicios de negocio de manera libre según la necesidad de la prueba técnica.