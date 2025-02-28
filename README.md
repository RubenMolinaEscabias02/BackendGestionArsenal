# Backend para el Sistema de Gestión de Armas

Este proyecto es una aplicación backend diseñada para gestionar armas, acciones y usuarios, utilizando DynamoDB como base de datos. Ofrece funcionalidades como registrar armas, modificarlas, registrar las acciones realizadas sobre las armas y gestionar usuarios. La aplicación está construida con Spring Boot y utiliza Spring Security para la autenticación y autorización.

## Características

- **Gestión de Armas:**
  - Crear, actualizar y eliminar armas.
  - Recuperar todas las armas o filtrarlas por diferentes atributos (por ejemplo, tipo, nombre, calibre, año de fabricación).
  - Marcar armas como obsoletas.
  
- **Gestión de Acciones:**
  - Registrar las acciones realizadas sobre las armas (por ejemplo, inserción, modificación, eliminación).
  - Recuperar acciones en diferentes órdenes (por ejemplo, nuevas primero, viejas primero, acciones de hoy).
  - Filtrar acciones por tipo.

- **Gestión de Usuarios:**
  - Registro e inicio de sesión de usuarios.
  - Modificar información de usuario.
  - Eliminar usuarios.

- **Seguridad:**
  - Autenticación básica utilizando Spring Security.
  - Configuración de CORS para permitir compartir recursos entre diferentes orígenes.

## Tecnologías Utilizadas

- **Spring Boot** para la creación de la aplicación backend.
- **Spring Security** para asegurar los endpoints y gestionar la autenticación de usuarios.
- **DynamoDB** como base de datos para almacenar los datos de armas, acciones y usuarios.
- **BCryptPasswordEncoder** para el cifrado de contraseñas.
- **Java 22** para el desarrollo del backend.

## Endpoints

### /usuario (Gestión de Usuarios)

- **POST /register**: Registrar un nuevo usuario.
- **GET /login**: Iniciar sesión con el usuario y la contraseña.
- **GET /all**: Obtener todos los usuarios.
- **GET /id/{id}**: Obtener un usuario por su ID.
- **PUT /modify**: Modificar un usuario existente.
- **POST /delete/{id}**: Eliminar un usuario por su ID.

### /arma (Gestión de Armas)

- **POST /create-table**: Crear la tabla de armas en DynamoDB.
- **POST /add**: Añadir una nueva arma.
- **GET /all**: Obtener todas las armas.
- **GET /old**: Obtener armas ordenadas por año (más antiguas primero).
- **GET /new**: Obtener armas ordenadas por año (más nuevas primero).
- **GET /id/{id}**: Obtener una arma por su ID.
- **GET /obsolete/{obsoleto}**: Obtener armas por su estado de obsolescencia.
- **GET /type/{tipo}**: Obtener armas por tipo.
- **GET /name/{nombre}**: Obtener armas por nombre.
- **GET /caliber/{calibre}**: Obtener armas por calibre.
- **PUT /modify**: Modificar una arma existente.
- **POST /delete/{id}**: Eliminar una arma por su ID.

### /accion (Gestión de Acciones)

- **POST /create-table**: Crear la tabla de acciones en DynamoDB.
- **GET /new**: Obtener las acciones más recientes (más nuevas primero).
- **GET /old**: Obtener las acciones más antiguas (más viejas primero).
- **GET /today**: Obtener las acciones realizadas hoy.
- **GET /type/{tipo}**: Obtener las acciones por tipo.
- **GET /id/{id}**: Obtener una acción por su ID.
- **POST /delete/{id}**: Eliminar una acción por su ID.

## Seguridad

Esta aplicación utiliza **Autenticación Básica** con un usuario almacenado en memoria. Las credenciales (nombre de usuario y contraseña) se obtienen desde las variables de entorno y la contraseña se cifra utilizando **BCrypt**.

### Configuración de CORS

CORS está habilitado y puede configurarse a través de la variable de entorno `SPRING_CORS_ORIGIN`.

## Cómo Ejecutar

### Requisitos

- Java 22 o posterior
- Maven
- Credenciales del cliente de DynamoDB (para acceder a DynamoDB)

### Pasos

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tuusuario/weapon-management-backend.git
   cd weapon-management-backend
2. **Configurar las variables de entorno para seguridad y CORS**:

  - **SPRING_SECURITY_USER_NAME**: Configurar el nombre de usuario para la autenticación.
  - **SPRING_SECURITY_USER_PASSWORD**: Configurar la contraseña para la autenticación.
  - **SPRING_CORS_ORIGIN**: Configurar los orígenes permitidos para CORS.
  - **SPRING_DB_URL**: La URL del endpoint de DynamoDB. Esta URL debe apuntar a tu instancia de DynamoDB (por ejemplo, si usas DynamoDB Local o el servicio en la nube de AWS).
  - **SPRING_DB_ACCESS_KEY_ID**: El Access Key ID de tu cuenta de AWS o el proporcionado por tu instancia de DynamoDB Local.
  - **SPRING_DB_SECRET_ACCESS_KEY**: El Secret Access Key de tu cuenta de AWS o el proporcionado por tu instancia de DynamoDB Local.
3. **Ejecutar el archivo GestionArsenal.bat**
4. **Acceder a la aplicación en** http://localhost:8080, en caso de no acceder de forma local cambia localhost por la ip de tu servidor.
