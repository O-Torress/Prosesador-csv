# CSVLib Java

CSVLib Java es una pequeña librería orientada a objetos para trabajar con archivos CSV mediante un modelo de tabla inmutable por composición y una fachada de alto nivel llamada `CsvDocument`.

## 1. ¿Qué ofrece la librería?

La biblioteca se apoya en estos conceptos principales:

- `Table`: representa la estructura tabular (`headers` + `rows`) y sirve como modelo de datos central.
- `CsvDocument`: fachada que encapsula lectura, modificación, renderizado y escritura.
- `IReader`: define cómo cargar una fuente CSV.
- `IWriter`: define cómo guardar una tabla en un destino.
- `IRenderer`: define cómo presentar la tabla en consola u otro formato.
- `IOperation`: define transformaciones sobre la tabla.
- `IMiddleware`: permite interceptar o modificar el resultado del pipeline sin alterar la lógica base.

La idea de diseño es que la librería sea extensible mediante nuevas implementaciones de interfaces, en lugar de modificar las clases internas existentes.

## 2. Estructura del proyecto

La parte principal del código fuente se encuentra en:

- `csvlib-java/src/ejemplos/main/CsvDocumento.java` — fachada pública de uso.
- `csvlib-java/src/ejemplos/main/Tabla.java` — modelo de la tabla.
- `csvlib-java/src/ejemplos/main/IReader.java` — interfaz para lectura.
- `csvlib-java/src/ejemplos/main/IWriter.java` — interfaz para escritura.
- `csvlib-java/src/ejemplos/main/IRenderer.java` — interfaz para renderizado.
- `csvlib-java/src/ejemplos/main/IOperation.java` — interfaz para transformaciones.
- `csvlib-java/src/ejemplos/main/IMiddleware.java` — interfaz para middleware.

## 3. Cómo usar la librería

### 3.1. Cargar un CSV

```java
import com.csvlib.facade.CsvDocument;

CsvDocument doc = CsvDocument.load("datos.csv");
```

Si quieres usar un lector personalizado:

```java
CsvDocument doc = CsvDocument.load("datos.csv", new MiReader());
```

### 3.2. Modificar la tabla

La fachada permite encadenar operaciones de forma cómoda:

```java
CsvDocument doc = CsvDocument.load("datos.csv")
    .addColumn("moneda", "USD")
    .addRow(List.of("4", "Agua", "1.00", "USD"))
    .removeRow(1);
```

También puedes trabajar directamente sobre la tabla construida:

```java
Table table = doc.build();
```

### 3.3. Mostrar la salida

```java
doc.print();
```

Esto usa el renderer por defecto (`ConsoleRenderer`). Si quieres cambiar la forma de presentación:

```java
CsvDocument doc = new CsvDocument(table, new MiRenderer());
doc.print();
```

### 3.4. Guardar el resultado

```java
doc.save("salida.csv");
```

O con un escritor propio:

```java
doc.save("salida.csv", new MiWriter());
```

## 4. Ejemplo completo de uso

```java
import com.csvlib.core.Table;
import com.csvlib.facade.CsvDocument;
import com.csvlib.io.MarkdownRenderer;

import java.util.Arrays;
import java.util.List;

public class BasicUsage {
    public static void main(String[] args) {
        String csv = "id,producto,precio\n1,Cafe,3.50\n2,Te,2.00\n3,Jugo,4.25";

        CsvDocument doc = CsvDocument.load(csv, new CsvStringReader());

        doc.addColumn("moneda", "USD")
           .addRow(Arrays.asList("4", "Agua", "1.00", "USD"))
           .removeRow(1);

        System.out.println("== Consola ==");
        doc.print();

        Table result = doc.getTable();
        CsvDocument doc2 = new CsvDocument(result, new MarkdownRenderer());
        doc2.print();
    }
}
```

## 5. Cómo extender la librería sin tocar el código base

La librería está preparada para crecer por extensión, no por modificación. La clave está en implementar las interfaces y registrarlas a través de `CsvDocument`.

### 5.1. Crear un lector nuevo

Implementa `IReader`:

```java
package com.miapp.io;

import com.csvlib.core.IReader;
import com.csvlib.core.Table;

public class JsonReader implements IReader {
    @Override
    public Table read(String source) {
        // Convierte JSON o cualquier fuente a Table
        return new Table(new java.util.ArrayList<>(), new java.util.ArrayList<>());
    }
}
```

Uso:

```java
CsvDocument doc = CsvDocument.load("datos.json", new JsonReader());
```

### 5.2. Crear un escritor nuevo

Implementa `IWriter`:

```java
package com.miapp.io;

import com.csvlib.core.IWriter;
import com.csvlib.core.Table;

public class HtmlWriter implements IWriter {
    @Override
    public void write(Table table, String target) {
        // Guarda la tabla en HTML
    }
}
```

Uso:

```java
doc.save("reporte.html", new HtmlWriter());
```

### 5.3. Crear un renderer nuevo

Implementa `IRenderer`:

```java
package com.miapp.render;

import com.csvlib.core.IRenderer;
import com.csvlib.core.Table;

public class CsvTableRenderer implements IRenderer {
    @Override
    public String render(Table table) {
        // Genera una cadena formateada para CSV, HTML, texto, etc.
        return "";
    }
}
```

Uso:

```java
new CsvDocument(table, new CsvTableRenderer()).print();
```

### 5.4. Crear una operación nueva

Implementa `IOperation`:

```java
package com.miapp.operations;

import com.csvlib.core.IOperation;
import com.csvlib.core.Table;

public class UppercaseNames implements IOperation {
    @Override
    public Table apply(Table table) {
        // Transformación sobre table
        return table;
    }
}
```

Uso:

```java
CsvDocument doc = CsvDocument.load("datos.csv");
doc.apply(new UppercaseNames());
```

### 5.5. Crear middleware para interceptar el flujo

Implementa `IMiddleware`:

```java
package com.miapp.middleware;

import com.csvlib.core.IMiddleware;
import com.csvlib.core.Table;

import java.util.function.Function;

public class LoggingMiddleware implements IMiddleware {
    @Override
    public Table process(Table table, Function<Table, Table> next) {
        System.out.println("Antes de ejecutar pipeline: " + table);
        Table result = next.apply(table);
        System.out.println("Después de ejecutar pipeline: " + result);
        return result;
    }
}
```

Uso:

```java
doc.use(new LoggingMiddleware());
```

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
