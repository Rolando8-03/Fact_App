# Fact_App actualizado

Rolando Enrique Mayorga Mena
Programación de Aplicaciones de Escritorio — MSc. José Alejandro Durán García

Integración basada en tu proyecto y en los dos ZIP de referencia proporcionados. Se conserva el paquete ni.edu.uam.fact_app, los formularios claros, el logo, los iconos, la distribución original de campos y el resumen del menú.

## Preparar PostgreSQL

1. En pgAdmin, abra Query Tool conectado a la base `postgres`.
2. Ejecute únicamente `sql/01_crear_base.sql`, con Auto-commit activo. CREATE DATABASE no debe ejecutarse dentro de BEGIN/COMMIT. Si ya existe `fact_app`, omita este paso.
3. Actualice el árbol y abra otro Query Tool conectado a `fact_app`.
4. Ejecute `sql/02_tablas.sql`. Está preparado para una base nueva; CREATE TABLE IF NOT EXISTS no adapta columnas de tablas antiguas con una estructura diferente.
5. Opcionalmente, ejecute `sql/03_datos_ejemplo_opcional.sql` para tener categorías, cargos y un producto de prueba.
6. Edite `config/database.properties` y escriba su contraseña de PostgreSQL después de `db.password=`. Ajuste puerto, nombre de base y usuario si corresponden.

```properties
db.url=jdbc:postgresql://localhost:5432/fact_app
db.user=postgres
db.password=SU_CONTRASENA
```

La contraseña de PostgreSQL no es la contraseña del formulario de inicio de sesión. Los archivos se leen como Java Properties: si una contraseña contiene una barra invertida, escríbala duplicada. También se admiten las variables FACT_DB_URL, FACT_DB_USER y FACT_DB_PASSWORD, con prioridad sobre el archivo.

## Abrir y ejecutar

1. Extraiga todo el ZIP.
2. En IntelliJ, abra el `pom.xml` como proyecto Maven.
3. Seleccione JDK 21 para el proyecto y para Maven; recargue las dependencias.
4. Use como directorio de trabajo la carpeta que contiene `pom.xml`.
5. Ejecute `ni.edu.uam.fact_app.application.FacturacionApplication`, o el objetivo Maven `javafx:run`.

En Windows también puede ejecutar `iniciar.bat` con JAVA_HOME apuntando al JDK 21. La primera ejecución necesita Internet para descargar Maven y las dependencias.

## Cuentas de práctica

| Usuario | Contraseña | Módulos habilitados |
| --- | --- | --- |
| admin | admin123 | Todos |
| cajero | cajero123 | Productos y ventas |
| bodega | bodega123 | Productos y categorías |

Se conserva el mecanismo de cuentas de demostración de la referencia. Estas cuentas están definidas en LoginController, no se crean desde Empleados y no constituyen un sistema de administración de usuarios. La factura guarda el nombre del usuario y del vendedor de la sesión.

## Uso

- **Guardar:** agrega un registro nuevo. Al seleccionar una fila, cambia a **Actualizar**.
- **Nuevo:** limpia el formulario y sale del modo de edición.
- **Eliminar:** solicita confirmación. Las relaciones impiden eliminar registros utilizados; puede desactivar categorías, productos o empleados.
- **Buscar:** permite filtrar por ID exacto o por nombre; productos también permite código. El diálogo Buscar ofrece ID o nombre.
- **Restablecer:** quita los filtros.
- **Refrescar:** recarga desde PostgreSQL y limpia el formulario; úselo después de guardar o cuando no necesite conservar cambios pendientes.
- **Categorías:** actualización automática de la tabla cada 2.5 segundos, pausada durante la edición o mientras escribe un nuevo nombre. Se detiene al cerrar la ventana.
- **Productos:** filtros por estado y categoría; selección y vista previa de imágenes. Las imágenes nuevas se copian a `data/imagenes`. Conserve esa carpeta al mover el proyecto.
- **Empleados:** nombres, apellidos, cargo, fecha y estado; no admite fechas de contratación futuras.
- **Ventas:** busque un producto, seleccione cantidad, agregue artículos y confirme Finalizar venta. Se valida la existencia acumulada y se calcula subtotal, IVA del 15 % y total, siguiendo la lógica de la referencia.

La factura, sus detalles y el descuento de inventario se guardan en una sola transacción. El número se asigna al guardar y permanece en PostgreSQL. Las ventas registradas pueden consultarse con `sql/04_consultar_ventas.sql`.

## Organización del código

- `controller`: interacción de los siete formularios.
- `dao`: consultas parametrizadas y operaciones de PostgreSQL.
- `model`: categorías, cargos, empleados, productos, usuarios y ventas.
- `util`: conexión, navegación, sesión, alertas, búsqueda y actualización automática.
- `Crud<T>` y sus cuatro implementaciones se conservan como adaptadores a los DAO; los formularios nuevos utilizan los DAO directamente.
- `src/main/resources`: FXML, imágenes e iconos.

## Verificación realizada

Compilación modular con Java 21 y construcción Maven: BUILD SUCCESS.
Se ejecutaron 31 comprobaciones con JavaFX 21.0.6 y una base PostgreSQL de prueba mediante PGlite (PostgreSQL en WebAssembly), incluyendo carga y renderizado de las siete pantallas, CRUD, filtros, permisos, factura y reversión de cambios cuando falla una venta. Se revisaron visualmente las pantallas y se corrigieron textos recortados.

La conexión a su instalación de PostgreSQL y la ejecución en su Windows requieren la configuración indicada arriba; no se accedió a su equipo ni a sus datos locales. No se incluyen los datos personales de sus compañeros.
