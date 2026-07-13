# CSVLib Java

CSVLib Java es una pequeña librería orientada a objetos para procesar tablas CSV mediante una fachada `CsvDocument` y un modelo `Table`.

## 1. ¿Qué ofrece la librería?

La versión actual del proyecto está organizada en torno a estos conceptos:

- `Table`: representa la estructura tabular (`headers` + `rows`).
- `CsvDocument`: fachada de alto nivel para cargar, modificar, renderizar y guardar una tabla.
- `IReader`, `IWriter`, `IRenderer`: contratos para abstraer lectura, escritura y presentación.
- `IOperation`: contrato para transformaciones aplicables sobre una tabla.
- `IMiddleware`: extensión del flujo del pipeline mediante interceptores.

La intención de diseño es seguir un enfoque extensible por composición e interfaces.

## 2. Estructura actual del proyecto

La implementación real del proyecto está distribuida así:

- `csvlib-java/src/ejemplos/main/CsvDocument.java` — fachada pública de uso.
- `csvlib-java/src/ejemplos/main/Table.java` — modelo de la tabla.
- `csvlib-java/src/ejemplos/main/IReader.java` — interfaz para lectura.
- `csvlib-java/src/ejemplos/main/IWriter.java` — interfaz para escritura.
- `csvlib-java/src/ejemplos/main/IRenderer.java` — interfaz para renderizado.
- `csvlib-java/src/ejemplos/main/IOperation.java` — interfaz para transformaciones.
- `csvlib-java/src/ejemplos/main/IMiddleware.java` — interfaz para middleware.
- `csvlib-java/src/com/csvlib/io/` — implementaciones concretas de reader, writer y renderers.
- `csvlib-java/src/com/csvlib/operations/` — operaciones y pipeline.
- `csvlib-java/src/ejemplos/BasicUsage.java` — ejemplo de uso.

## 3. Cómo usar la librería

### 3.1. Cargar un CSV desde texto

```java
import com.csvlib.facade.CsvDocument;
import com.csvlib.io.CsvStringReader;

String csv = "id,producto,precio\n1,Cafe,3.50\n2,Te,2.00\n3,Jugo,4.25";
CsvDocument doc = CsvDocument.load(csv, new CsvStringReader());
```

### 3.2. Modificar la tabla

```java
CsvDocument doc = CsvDocument.load(csv, new CsvStringReader())
    .addColumn("moneda", "USD")
    .addRow(List.of("4", "Agua", "1.00", "USD"))
    .removeRow(1);
```

### 3.3. Mostrar la salida por consola

```java
doc.print();
```

### 3.4. Renderizar en Markdown

```java
Table result = doc.getTable();
CsvDocument doc2 = new CsvDocument(result, new MarkdownRenderer());
doc2.print();
```

### 3.5. Guardar la salida en un archivo CSV

```java
doc.save("salida.csv");
```

## 4. Ejemplo completo

```java
package com.csvlib.examples;

import com.csvlib.core.Table;
import com.csvlib.facade.CsvDocument;
import com.csvlib.io.CsvStringReader;
import com.csvlib.io.MarkdownRenderer;

import java.util.Arrays;

public class BasicUsage {
    public static void main(String[] args) {
        String csv = "id,producto,precio\n1,Cafe,3.50\n2,Te,2.00\n3,Jugo,4.25";

        CsvDocument doc = CsvDocument.load(csv, new CsvStringReader());

        doc.addColumn("moneda", "USD")
           .addRow(Arrays.asList("4", "Agua", "1.00", "USD"))
           .removeRow(1);

        System.out.println("== Consola ==");
        doc.print();

        System.out.println("\n== Markdown ==");
        Table result = doc.getTable();
        CsvDocument doc2 = new CsvDocument(result, new MarkdownRenderer());
        doc2.print();
    }
}
```

## 5. Extensión del proyecto

La librería está preparada para ampliarse creando nuevas implementaciones de las interfaces del núcleo:

- `IReader` para nuevas fuentes de entrada.
- `IWriter` para nuevos destinos de salida.
- `IRenderer` para nuevas representaciones visuales.
- `IOperation` para nuevas transformaciones.
- `IMiddleware` para interceptar o modificar el flujo del pipeline.

## 6. Estado actual

El proyecto ya está compilable y el ejemplo principal se ejecuta correctamente con una salida válida en consola y Markdown.


## 6. Principios de diseño que hacen extensible la librería

La librería sigue varias buenas prácticas de diseño:

- `Facade`: `CsvDocument` ofrece una API simple sobre piezas más pequeñas.
- `Dependency Inversion`: la fachada depende de interfaces (`IReader`, `IWriter`, `IRenderer`, `IOperation`), no de implementaciones concretas.
- `Open/Closed Principle`: nuevas operaciones y renderizadores pueden añadirse sin modificar la lógica central.
- `Single Responsibility`: `Table` representa datos, `IReader` lee, `IWriter` escribe, `IRenderer` renderiza.

## 7. Recomendaciones prácticas

- Usa `CsvDocument` como punto de entrada principal.
- Mantén las transformaciones en clases separadas (`IOperation`).
- Si necesitas una salida distinta, implementa `IRenderer` o `IWriter` en lugar de tocar `CsvDocument`.
- Si necesitas modificar el flujo antes o después de una operación, usa `IMiddleware`.
- Mantén `Table` como el único modelo de datos de la librería para evitar acoplamientos innecesarios.

## 8. Resumen

Para usar la librería, basta con:

1. Crear o cargar un `CsvDocument`.
2. Encadenar operaciones básicas o personalizadas.
3. Mostrar o guardar la salida final.

Para extenderla sin romper el código base:

1. Implementa una interfaz nueva (`IReader`, `IWriter`, `IRenderer`, `IOperation` o `IMiddleware`).
2. Registra la implementación en tu flujo de uso.
3. Deja intacto el núcleo de la librería.

Si deseas, esta guía puede convertirse luego en un README más formal con badges, ejemplos de instalación, y una sección de arquitectura gráfica.
