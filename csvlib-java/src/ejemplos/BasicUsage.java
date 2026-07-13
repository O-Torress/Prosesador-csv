package com.csvlib.examples;

import com.csvlib.core.Table;
import com.csvlib.facade.CsvDocument;
import com.csvlib.io.CsvStringReader;
import com.csvlib.io.MarkdownRenderer;

import java.util.Arrays;
import java.util.List;

/** Uso basico de csvlib: cargar, editar filas/columnas e imprimir. */
public class BasicUsage {

    public static void main(String[] args) {
        String csv = "id,producto,precio\n1,Cafe,3.50\n2,Te,2.00\n3,Jugo,4.25";

        CsvDocument doc = CsvDocument.load(csv, new CsvStringReader());

        // Editar: agregar columna, agregar fila, quitar fila
        doc.addColumn("moneda", "USD");
        doc.addRow(Arrays.asList("4", "Agua", "1.00", "USD"));
        doc.removeRow(1); // quita el Te

        System.out.println("== Consola ==");
        doc.print();

        // Reutilizar la tabla resultante con otro renderer
        System.out.println("\n== Markdown ==");
        Table result = doc.getTable();
        CsvDocument doc2 = new CsvDocument(result, new MarkdownRenderer());
        doc2.print();
    }
}
