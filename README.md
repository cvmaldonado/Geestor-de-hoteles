# 🏨 HotelS - Sistema de Gestión Hotelera

**HotelS** es una aplicación web desarrollada con **Jakarta EE**, **JSF** y **Payara Server**, diseñada para gestionar de manera integral la operación de un hotel: habitaciones, reservas, huéspedes y estadísticas del sistema.

---

## 🚀 Tecnologías Utilizadas

- **Java 21**
- **Jakarta EE 10**
- **Payara Server 6**
- **PostgreSQL 15**
- **Maven** para gestión de dependencias
- **JAX-RS Client API** para comunicación entre la capa web y los endpoints REST
- **HTML5 + CSS3** (interfaces modernas con Facelets)

---

## 🧩 Arquitectura del Proyecto

El proyecto está dividido en los siguientes módulos/layers:


---

## ⚙️ Configuración del Entorno

### 1️⃣ Base de Datos PostgreSQL

Crea una base de datos llamada `DBHotel` (o el nombre que prefieras):

## ⚙️ Configuración en Payara Server

Debes crear una **JDBC Connection Pool** y un **JDBC Resource** para que la aplicación pueda conectarse a la base de datos desde el servidor.

---

### 🔹 Paso 1: Crear el Connection Pool

En la consola de administración de **Payara Server** (`http://localhost:4848`):

1. Ir a **Resources → JDBC → JDBC Connection Pools**  
2. Click en **New...**
3. Asigna los siguientes valores:

   | Campo | Valor |
   |--------|--------|
   | **Pool Name** | `HotelSPool` |
   | **Resource Type** | `javax.sql.DataSource` |
   | **Database Vendor** | `PostgreSQL` |

4. Click en **Next**, y configura los siguientes parámetros:

User: postgres
Password: tu_contraseña
URL: jdbc:postgresql://localhost:5432/hotels_db
Driver Classname: org.postgresql.Driver


---

### 🔹 Paso 2: Crear el JDBC Resource

1. Ir a **Resources → JDBC → JDBC Resources**  
2. Click en **New...**
3. En **JNDI Name**, escribe:

jdbc/hotelsDS
En Pool Name, selecciona:
HotelSPool


🔹 Paso 3: Configuración en persistence.xml
