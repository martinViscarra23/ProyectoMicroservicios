# 🛒 E-Commerce Microservices Architecture | Spring Cloud & Docker
![Java 21](https://img.shields.io/badge/Java%2021-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-6DB33F?style=flat-square&logo=spring&logoColor=white)
![OpenFeign](https://img.shields.io/badge/OpenFeign-REST%20Client-4479A1?style=flat-square&logo=spring&logoColor=white)
![Resilience4j](https://img.shields.io/badge/Resilience4j-Circuit%20Breaker-8A2BE2?style=flat-square&logo=sentry&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white)

Este repositorio contiene el desarrollo backend de una plataforma de comercio electrónico de electrodomésticos basada en una arquitectura de microservicios altamente escalable, resiliente y containerizada. El sistema está diseñado e implementado aplicando los patrones de diseño fundamentales del ecosistema **Spring Cloud**, enfocándose en la tolerancia a fallos, el balanceo de carga dinámico y la separación de responsabilidades de negocio.

---

## 🏗️ Arquitectura del Sistema

El ecosistema está compuesto por un servidor de registro central, un API Gateway como punto único de entrada y tres microservicios de dominio que gestionan de forma aislada sus propias bases de datos relacionales en MySQL.

![Arquitectura del Sistema](/docs/arquitectura.png)

### Ecosistema de Microservicios
* **API Gateway (Puerto `443`):** Servidor reactivo basado en **Spring Cloud Gateway (WebFlux)** y Netty. Gestiona el ruteo de peticiones externas y centraliza la documentación interactiva de todo el ecosistema.
* **Eureka Server (Puerto `8761`):** Servidor de descubrimiento y registro (**Service Registry & Discovery**). Permite que los microservicios se localicen entre sí dinámicamente mediante nombres lógicos sin depender de direcciones IP o puertos estáticos.
* **Product Service:** Gestión del catálogo de productos y precios individuales. Diseñado como el servicio de mayor demanda, preparado para escalar horizontalmente de forma nativa.
* **Cart Service:** Administración de los carritos de compra de los usuarios y gestión de ítems agregados.
* **Sale Service:** Procesamiento y registro histórico de transacciones. 

### Principios de Diseño y Dominio
El modelado de datos y negocio respeta límites estrictos de responsabilidad y normalización:
* **Desacoplamiento de precios:** Los carritos de compra no almacenan precios ni totales redundantes. El costo final se procesa dinámicamente consultando la disponibilidad y precio en tiempo real, garantizando consistencia y evitando datos obsoletos.
* **Autonomía transaccional:** Las ventas no mantienen una dependencia de persistencia rígida con un carrito activo. Una vez procesada la venta, la transacción conserva su propio registro inmutable de ítems adquiridos, permitiendo limpiar o liberar los carritos sin afectar el historial financiero.

---

## ⚙️ Patrones de Resiliencia y Tecnologias Implementedas

* **Comunicación Síncrona Desacoplada (OpenFeign):** Interconexión entre microservicios mediante clientes declarativos REST. Se implementó una arquitectura personalizada con `FeignClientConfig` y **ErrorDecoders** para interceptar respuestas HTTP del servidor destino y transformarlas en excepciones de dominio nativas (ej. mapeo de `404 Not Found` a `EntityNotFoundException`).
* **Tolerancia a Fallos y Circuit Breaker (Resilience4j):** Protección del sistema ante caídas en cadena. El circuito está configurado estructuralmente para **diferenciar errores de negocio de fallos de infraestructura**: las excepciones de dominio (como un producto no encontrado) no contabilizan para la apertura del circuito, reservando los estados *Open* y *Half-Open* exclusivamente para caídas reales de servidores, timeouts o pérdida de conectividad.
* **Reintentos Automáticos (`@Retry`):** Políticas de reintento configuradas ante fallos intermitentes de red antes de ejecutar los métodos de *Fallback*.
* **Balanceo de Carga en el Cliente (Spring Cloud LoadBalancer):** Distribución equitativa del tráfico interno y externo mediante el algoritmo **Round Robin** en el servicio de Productos. El sistema permite levantar múltiples instancias simultáneas de dicho servicio sin modificación de código ni conflictos de puertos.
* **Containerización Total (Docker & Docker Compose):** Orquestación completa del entorno de desarrollo. Cada microservicio corre en un contenedor aislado con **Java 21**, comunicándose dentro de una red privada de Docker (`microservices-net`) con una instancia de **MySQL 8** automatizada mediante scripts de inicialización (`init.sql`).

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 21
* **Framework Core:** Spring Boot 3.5+
* **Cloud & Microservicios:** Spring Cloud (Eureka, Gateway WebFlux, OpenFeign, LoadBalancer, )
* **Resiliencia:** Resilience4j (Circuit Breaker, Retry)
* **Base de Datos & ORM:** MySQL 8.0, Spring Data JPA, Hibernate
* **DevOps & Infraestructura:** Docker
* **Documentación:** OpenAPI 3 / Swagger UI

---

# 🚀 Ejecución

El proyecto puede ejecutarse completamente utilizando únicamente Docker y Git. No es necesario instalar MySQL ni configurar dependencias adicionales.

## 1. Clonar el repositorio

```bash
git clone https://github.com/martinViscarra23/ProyectoMicroservicios.git

cd ProyectoMicroservicios
```

## 2. Levantar toda la infraestructura (Docker)

```bash
docker compose up --build -d
```
Espere unos segundos y podrá ver en el panel de Eureka (`http://localhost:8761`) todos los microservicios registrados automáticamente.


## 📈 Escalado Horizontal (Opcional)

Para comprobar el balanceo de carga, puede iniciarse múltiples instancias del Product Service.

```bash
docker compose up --build -d \
--scale product-service=3 \
--no-recreate
```

En el panel de Eureka (`http://localhost:8761`) podrá observar cómo las tres instancias son registradas automáticamente.

---

# 📖 Documentación de la API

## Swagger UI

Con todos los contenedores iniciados:

```
http://localhost:443/webjars/swagger-ui/index.html
```

Desde el API Gateway es posible alternar dinámicamente entre:

- Product Service
- Cart Service
- Sale Service

Todas las solicitudes pasan por el Gateway utilizando el puerto **443**.

---
