package com.csvlib.facade;

import com.csvlib.core.IMiddleware;
import com.csvlib.core.IOperation;
import com.csvlib.core.IReader;
import com.csvlib.core.IRenderer;
import com.csvlib.core.IWriter;
import com.csvlib.core.Table;
import com.csvlib.io.ConsoleRenderer;
import com.csvlib.io.CsvReader;
import com.csvlib.io.CsvWriter;
import com.csvlib.operations.Operations;
import com.csvlib.operations.Pipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Fachada de alto nivel de la libreria.
 *
 * Ofrece la API comoda que pide el enunciado ("procesamiento de archivos CSV
 * mediante metodos de un objeto") mientras internamente delega en las
 * abstracciones.
 *
 * Patron Facade: simplifica el subsistema (Table + IO + Pipeline) tras una
 * interfaz unica y encadenable.
 *
 * DIP: depende de IReader, IWriter, IRenderer y de Pipeline; las dependencias
 * concretas se inyectan por constructor con valores por defecto.
 */
public class CsvDocument {

    private final IReader reader;
    private final IWriter writer;
    private final IRenderer renderer;
    private Table table;
    private Pipeline pipeline;

    // ------------------------------------------------------------------ //
    // Construccion
    // ------------------------------------------------------------------ //
    public CsvDocument() {
        this(null, null, null, null);
    }

    public CsvDocument(Table table, IReader reader, IWriter writer, IRenderer renderer) {
        this.reader = (reader != null) ? reader : new CsvReader();
        this.writer = (writer != null) ? writer : new CsvWriter();
        this.renderer = (renderer != null) ? renderer : new ConsoleRenderer();
        this.table = (table != null) ? table : new Table(new ArrayList<>(), new ArrayList<>());
        this.pipeline = new Pipeline();
    }

    /** Constructor de conveniencia para envolver una tabla ya existente. */
    public CsvDocument(Table table) {
        this(table, null, null, null);
    }

    /** Constructor de conveniencia para fijar un renderer. */
    public CsvDocument(Table table, IRenderer renderer) {
        this(table, null, null, renderer);
    }

    /** Carga un CSV desde una fuente usando el lector indicado (o CsvReader). */
    public static CsvDocument load(String source, IReader reader) {
        IReader r = (reader != null) ? reader : new CsvReader();
        CsvDocument doc = new CsvDocument(null, r, null, null);
        doc.table = r.read(source);
        return doc;
    }

    public static CsvDocument load(String source) {
        return load(source, null);
    }

    // ------------------------------------------------------------------ //
    // Operaciones basicas (encolan comandos en el pipeline)
    // ------------------------------------------------------------------ //
    public CsvDocument addRow(List<String> row) {
        pipeline.add(new Operations.AddRow(row));
        return this;
    }

    public CsvDocument addRow(List<String> row, Integer index) {
        pipeline.add(new Operations.AddRow(row, index));
        return this;
    }

    public CsvDocument removeRow(int index) {
        pipeline.add(new Operations.RemoveRow(index));
        return this;
    }

    public CsvDocument addColumn(String name, List<String> values, String defaultValue) {
        pipeline.add(new Operations.AddColumn(name, values, defaultValue));
        return this;
    }

    public CsvDocument addColumn(String name, String defaultValue) {
        pipeline.add(new Operations.AddColumn(name, defaultValue));
        return this;
    }

    public CsvDocument removeColumn(String name) {
        pipeline.add(new Operations.RemoveColumn(name));
        return this;
    }

    // ------------------------------------------------------------------ //
    // Puntos de extension (Abierto/Cerrado)
    // ------------------------------------------------------------------ //
    /** Encola CUALQUIER operacion propia del usuario. */
    public CsvDocument apply(IOperation operation) {
        pipeline.add(operation);
        return this;
    }

    /** Registra un middleware que modifica el resultado final. */
    public CsvDocument use(IMiddleware middleware) {
        pipeline.use(middleware);
        return this;
    }

    // ------------------------------------------------------------------ //
    // Materializacion y salida
    // ------------------------------------------------------------------ //
    /** Ejecuta el pipeline y devuelve la tabla resultante. */
    public Table build() {
        this.table = pipeline.run(this.table);
        this.pipeline = new Pipeline(); // se reinicia tras aplicar
        return this.table;
    }

    public String print() {
        return print(null);
    }

    public String print(IRenderer customRenderer) {
        build();
        IRenderer r = (customRenderer != null) ? customRenderer : this.renderer;
        String text = r.render(this.table);
        System.out.println(text);
        return text;
    }

    public void save(String target) {
        save(target, null);
    }

    public void save(String target, IWriter customWriter) {
        build();
        IWriter w = (customWriter != null) ? customWriter : this.writer;
        w.write(this.table, target);
    }

    public Table getTable() {
        return table;
    }
}
