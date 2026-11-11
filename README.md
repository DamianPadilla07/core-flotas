#  Sistema Multi-Cliente de Gestión y Auditoría de Flotas - Administración MVC

**Autor:** Damián Padilla  
**Módulo:** Panel de Administración (Alimentación de Datos para el Core Financiero)  
**Materia:** Ingeniería Web

---

##  Descripción del Proyecto
Este repositorio contiene la arquitectura completa (Frontend y Backend) del módulo de Administración MVC. Este panel es el punto de entrada estructurado que alimenta de datos reales e íntegros al **Motor Core de Rentabilidad**.

El Core del sistema requiere evaluar si los vehículos son rentables, están en observación o deben darse de baja cruzando gastos operativos, coberturas de seguro y límites de depreciación. Para que esta fórmula matemática no falle, la entrada de datos (Instituciones, Aseguradoras, Pólizas y Vehículos) debe estar estrictamente validada desde su origen.

---

##  Cumplimiento Estricto de la Rúbrica (Tarea 8)

### 1. Validación Back-End de Datos Sensibles 
El atributo más crítico para el motor de evaluación es el **Umbral de Depreciación** del vehículo (el divisor en la fórmula de Porcentaje de Riesgo).
- **Implementación:** Se configuró una validación estricta en el Backend mediante la anotación `@DecimalMin(value = "1000.00")` en el modelo `Vehiculo.java` de Spring Boot.
- **Propósito:** Incluso si se evade la validación del lado del cliente (JS/HTML), el servidor interceptará y rechazará cualquier intento de registrar un umbral irreal (ej. $0), lo cual provocaría una excepción de división por cero y rompería el algoritmo del Core.

### 2. Integridad Referencial y Relaciones (Dropdowns) 
No se permite el ingreso manual de claves foráneas. El sistema respeta la arquitectura de base de datos relacional.
- **Implementación:** Al registrar un Vehículo, este debe pertenecer obligatoriamente a una `Institucion` cliente y estar cubierto por una `Poliza`. En la interfaz de Angular, estos campos no son inputs de texto simples, sino elementos `<select>` (Dropdowns) dinámicos.
- **Flujo:** El Frontend consume los endpoints `GET /api/instituciones` y `GET /api/polizas` para poblar las listas, garantizando que el vehículo se asocie únicamente a registros que ya existen y están validados en la base de datos de SQL Server.

### 3. Cero Datos Predeterminados 
El sistema se despliega en un estado inicial limpio. La configuración de Hibernate (`spring.jpa.hibernate.ddl-auto=create`) garantiza que la estructura de tablas se genere dinámicamente sin inyectar "Mock Data". Todo registro debe ser ingresado orgánicamente a través del flujo del panel MVC.

---

##  Stack Tecnológico

**Backend (Motor y API REST):**
- Java 22 / Spring Boot 3.x
- Spring Data JPA / Hibernate
- Base de Datos: SQL Server
- Arquitectura: MVC (Models, Repositories, Controllers)

**Frontend (Panel Administrativo):**
- Angular 18 (Standalone Components)
- Arquitectura SPA (Single Page Application)
- *Nota arquitectónica:* Se desactivó el Server-Side Rendering (SSR) ya que el sistema es un panel corporativo interno que prioriza la velocidad de interacción del lado del cliente sobre la indexación SEO.

---

##  Instrucciones de Ejecución Local

**1. Base de Datos:**
- Asegurarse de tener SQL Server corriendo localmente en el puerto `1433`.
- Crear una base de datos en blanco llamada `FlotasDB`.

**2. Backend (Spring Boot):**
- Abrir la carpeta `core-flotas` en IntelliJ IDEA.
- Configurar credenciales de SQL Server en `application.properties`.
- Ejecutar `CoreFlotasApplication.java`. (Iniciará en `localhost:8080`).

**3. Frontend (Angular):**
- Abrir la terminal en la carpeta `admin-flotas-front`.
- Ejecutar `npm install` para restaurar dependencias.
- Levantar el servidor de desarrollo con `ng serve`. (Iniciará en `localhost:4200`).