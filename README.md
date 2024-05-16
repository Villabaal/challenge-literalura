![Java Logo](https://logos-download.com/wp-content/uploads/2016/10/Java_logo_icon.png)
# Literalura Challenge - Catálogo de Libros

Este repositorio contiene mi solución para el desafío "Literalura" de Alura Latam, que consistió en desarrollar un catálogo de libros con interacción textual vía consola para los usuarios. El objetivo principal era proporcionar al menos 5 opciones de interacción, buscando libros a través de la API Gutedex y utilizando una base de datos PostgreSQL para la persistencia de datos.

## Descripción del Desafío

El objetivo del desafío "Literalura" era desarrollar un catálogo de libros que permitiera a los usuarios interactuar mediante texto en la consola. Esto implicaba:

- Implementar un sistema que permitiera a los usuarios buscar libros.
- Proporcionar al menos 5 opciones de interacción con el catálogo de libros.
- Utilizar la API Gutedex para obtener información sobre libros.
- Utilizar una base de datos PostgreSQL para almacenar y recuperar datos.

## Solución Propuesta

Para resolver este desafío, desarrollé una aplicación en Java utilizando el framework Spring. La solución se estructuró de la siguiente manera:

1. **Configuración del Proyecto Spring:** Se creó un proyecto Spring Boot para facilitar el desarrollo y la configuración.

2. **Integración con la API Gutedex:** Se utilizó la API Gutedex para obtener información sobre libros. Se implementó un cliente para realizar solicitudes a esta API y obtener los datos necesarios para el catálogo.

3. **Persistencia de Datos:** Se configuró una base de datos PostgreSQL para almacenar información sobre los libros. Se utilizaron entidades JPA para mapear los objetos Java a tablas de base de datos y se implementaron repositorios para acceder a los datos.

4. **Interacción con el Usuario:** Se implementó una interfaz de usuario textual en la consola que ofrece 9 opciones de interacción, como buscar libros por título, autor, etc. Se utilizaron menús interactivos y mensajes claros para guiar al usuario a través de las opciones disponibles.

## Tecnologías Utilizadas

- Java
- Spring Framework (Spring Boot, Spring Data JPA)
- PostgreSQL (Base de Datos)
- Gutedex API
- Dependencias Maven

## Relaciones entre Modelos

En este proyecto, se establece una relación bidireccional entre dos entidades principales: `Author` y `Book`.

### Modelo Author

El modelo `Author` representa a los autores de los libros en el catálogo. Cada autor puede tener varios libros asociados. Los campos en el modelo `Author` se corresponden con los datos proporcionados por la API Gutedex sobre los autores:

- `id`: Identificador único del autor.
- `name`: Nombre del autor.
- `birth_year`: Año de nacimiento.
- `death_year`: Año de nacimiento. (opcional)

### Modelo Book

El modelo `Book` representa los libros en el catálogo. Cada libro pertenece a un único autor. Los campos en el modelo `Book` se corresponden con los datos proporcionados por la API Gutedex sobre los libros:

- `id`: Identificador único del libro.
- `title`: Título del libro.
- `author`: Autor del libro (relación ManyToOne con el modelo `Author`).
- `language`: idioma del libro.
- `download_count`: numero de veces que ha sido descargado el modelo

### Relación OneToMany (Author - Book)

- Un autor puede tener varios libros asociados.
- Cada libro pertenece a un único autor.

La relación se implementa en el modelo `Author` mediante una lista de libros (`List<Book> books`), mientras que en el modelo `Book` se implementa mediante un campo de autor (`Author author`).

Esta estructura de datos permite una representación coherente de la relación entre autores y libros en el catálogo, lo que facilita la búsqueda y visualización de la información tanto para los usuarios como para el sistema.

![Spring Logo](https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Spring_Framework_Logo_2018.svg/2560px-Spring_Framework_Logo_2018.svg.png)
