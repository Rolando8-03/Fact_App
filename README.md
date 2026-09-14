# Fact_App

Aplicación de escritorio desarrollada en JavaFX como proyecto académico para la asignatura **Programación de Aplicaciones de Escritorio**.

El proyecto representa la base de un sistema de facturación y permite gestionar categorías, productos, cargos y empleados mediante diferentes formularios desarrollados con JavaFX y FXML.

## Estudiante

- Rolando Enrique Mayorga Mena

## Asignatura

**Programación de Aplicaciones de Escritorio**

**Docente:** MSc. José Alejandro Durán García

## Tecnologías utilizadas

- Java 21
- JavaFX 21.0.6
- FXML
- Scene Builder
- Maven
- Lombok
- IntelliJ IDEA
- Git y GitHub

## Funcionalidades

La aplicación cuenta actualmente con los siguientes módulos:

### Categorías

Permite:

- Registrar categorías.
- Indicar si una categoría está activa.
- Visualizar las categorías registradas.
- Mostrar todas las categorías o únicamente las activas.
- Conservar temporalmente los registros al cerrar y volver a abrir la ventana.

### Productos

Permite:

- Registrar código del producto.
- Registrar nombre.
- Seleccionar una categoría previamente registrada.
- Registrar precio de venta.
- Registrar existencia.
- Indicar si el producto está activo.
- Seleccionar una imagen para el producto.
- Visualizar los productos registrados.
- Mostrar todos los productos o únicamente los activos.
- Conservar temporalmente los registros al cerrar y volver a abrir la ventana.

### Cargos

Permite:

- Registrar el nombre de un cargo.
- Registrar una descripción.
- Visualizar los cargos registrados.
- Conservar temporalmente los registros al cerrar y volver a abrir la ventana.

### Empleados

Permite:

- Registrar nombres.
- Registrar apellidos.
- Seleccionar un cargo previamente registrado.
- Seleccionar la fecha de contratación.
- Validar que la fecha de contratación no sea posterior a la fecha actual.
- Indicar si el empleado está activo.
- Visualizar los empleados registrados.
- Conservar temporalmente los registros al cerrar y volver a abrir la ventana.

## Pantalla principal

La pantalla principal permite acceder a los diferentes módulos mediante una barra de herramientas.

Los botones utilizan íconos y muestran un `Tooltip` al colocar el cursor sobre ellos.

También se presenta un resumen con la cantidad de:

- Categorías registradas.
- Productos registrados.
- Cargos registrados.
- Empleados registrados.

El resumen se actualiza al regresar de cada módulo.

## CRUD

El proyecto utiliza una interfaz genérica `Crud<T>` con las operaciones:

```java
void guardar(T objeto);
ObservableList<T> listar();
void actualizar(int indice, T objeto);
void eliminar(T objeto);
```

Actualmente se implementan las siguientes clases:

```text
CategoriaCrud
ProductoCrud
CargoCrud
EmpleadoCrud
```

Estas clases utilizan listas temporales compartidas para conservar los registros mientras la aplicación continúa ejecutándose.

## Estructura del proyecto

```text
src/main
├── java
│   └── ni.edu.uam.fact_app
│       ├── application
│       │   └── FacturacionApplication.java
│       │
│       ├── controller
│       │   ├── MenuPrincipalController.java
│       │   ├── CategoriaController.java
│       │   ├── ProductoController.java
│       │   ├── CargoController.java
│       │   └── EmpleadoController.java
│       │
│       ├── model
│       │   ├── Categoria.java
│       │   ├── Producto.java
│       │   ├── Cargo.java
│       │   └── Empleado.java
│       │
│       └── util
│           ├── Crud.java
│           ├── CategoriaCrud.java
│           ├── ProductoCrud.java
│           ├── CargoCrud.java
│           ├── EmpleadoCrud.java
│           └── SceneManager.java
│
└── resources
    └── ni.edu.uam.fact_app
        ├── fxml
        │   ├── menu-principal.fxml
        │   ├── categoria-view.fxml
        │   ├── producto-view.fxml
        │   ├── cargo-view.fxml
        │   └── empleado-view.fxml
        │
        ├── images
        └── icons
```

## Validaciones

Los formularios incluyen validaciones para evitar registros incorrectos.

Entre ellas se encuentran:

- Validación de campos obligatorios.
- Precio mayor que cero.
- Existencia no negativa.
- Validación de valores numéricos.
- Fecha de contratación no posterior a la fecha actual.
- Mensajes de advertencia, error e información en español.

Los campos obligatorios se identifican mediante un asterisco `*`.

## Navegación

La aplicación utiliza `SceneManager` para abrir los diferentes formularios.

Las ventanas secundarias son modales y no permiten maximizarse ni cambiar manualmente su tamaño.

## Almacenamiento actual

Actualmente los datos se almacenan temporalmente utilizando `ObservableList`.

Esto significa que los registros permanecen disponibles al cerrar y volver a abrir los formularios mientras la aplicación siga ejecutándose.

Al cerrar completamente la aplicación, los registros se eliminan.

La incorporación de persistencia mediante una base de datos corresponde a una etapa posterior del proyecto.

## Ejecución

1. Clonar el repositorio.
2. Abrir el proyecto en IntelliJ IDEA.
3. Esperar a que Maven cargue las dependencias.
4. Verificar que el proyecto utilice Java 21.
5. Ejecutar `FacturacionApplication`.

## Repositorio

Proyecto desarrollado utilizando Git y GitHub para mantener un historial de cambios progresivo durante su desarrollo.
