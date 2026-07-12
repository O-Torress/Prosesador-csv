package com.csvlib.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Modelo de datos de la libreria.
 *
 * SRP (Responsabilidad Unica): esta clase SOLO representa y muta la estructura
 * tabular (encabezados + filas de String). No sabe leer archivos, ni imprimir
 * con formato, ni ejecutar pipelines; esas responsabilidades viven en otras
 * clases. Por defecto todos los datos se almacenan como String, segun el
 * requisito del enunciado.
 */
public class Table {

    private final List<String> headers;
    private final List<List<String>> rows;

    public Table(List<String> headers, List<List<String>> rows) {
        this.headers = new ArrayList<>();
        for (String h : headers) {
            this.headers.add(String.valueOf(h));
        }
        this.rows = new ArrayList<>();
        for (List<String> row : rows) {
            List<String> copy = new ArrayList<>();
            for (String c : row) {
                copy.add(String.valueOf(c));
            }
            this.rows.add(copy);
        }
    }

    // ------------------------------------------------------------------ //
    // Acceso de solo lectura (devuelve copias para proteger el estado)
    // ------------------------------------------------------------------ //
    public List<String> getHeaders() {
        return new ArrayList<>(headers);
    }

    public List<List<String>> getRows() {
        List<List<String>> out = new ArrayList<>();
        for (List<String> row : rows) {
            out.add(new ArrayList<>(row));
        }
        return out;
    }

    public int rowCount() {
        return rows.size();
    }

    public int columnCount() {
        return headers.size();
    }

    public int columnIndex(String name) {
        int idx = headers.indexOf(name);
        if (idx < 0) {
            throw new IllegalArgumentException("La columna '" + name + "' no existe");
        }
        return idx;
    }

    // ------------------------------------------------------------------ //
    // Mutacion de filas
    // ------------------------------------------------------------------ //
    public Table addRow(List<String> row) {
        return addRow(row, null);
    }

    public Table addRow(List<String> row, Integer index) {
        if (row.size() != headers.size()) {
            throw new IllegalArgumentException(
                "La fila tiene " + row.size() + " valores; se esperaban " + headers.size());
        }
        List<String> cells = new ArrayList<>();
        for (String c : row) {
            cells.add(String.valueOf(c));
        }
        if (index == null) {
            rows.add(cells);
        } else {
            rows.add(index, cells);
        }
        return this;
    }

    public Table removeRow(int index) {
        if (index < 0 || index >= rows.size()) {
            throw new IndexOutOfBoundsException("Indice de fila fuera de rango: " + index);
        }
        rows.remove(index);
        return this;
    }

    // ------------------------------------------------------------------ //
    // Mutacion de columnas
    // ------------------------------------------------------------------ //
    public Table addColumn(String name, List<String> values, String defaultValue) {
        if (headers.contains(name)) {
            throw new IllegalArgumentException("La columna '" + name + "' ya existe");
        }
        List<String> vals;
        if (values == null) {
            vals = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                vals.add(defaultValue);
            }
        } else {
            vals = values;
        }
        if (vals.size() != rows.size()) {
            throw new IllegalArgumentException(
                "Se dieron " + vals.size() + " valores; se esperaban " + rows.size());
        }
        headers.add(name);
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).add(String.valueOf(vals.get(i)));
        }
        return this;
    }

    public Table addColumn(String name, String defaultValue) {
        return addColumn(name, null, defaultValue);
    }

    public Table removeColumn(String name) {
        int idx = columnIndex(name);
        headers.remove(idx);
        for (List<String> row : rows) {
            row.remove(idx);
        }
        return this;
    }

    // ------------------------------------------------------------------ //
    // Utilidades
    // ------------------------------------------------------------------ //
    public Table copy() {
        return new Table(getHeaders(), getRows());
    }

    /** Devuelve cada fila como un mapa columna->valor (util para predicados). */
    public List<Map<String, String>> toMaps() {
        List<Map<String, String>> out = new ArrayList<>();
        for (List<String> row : rows) {
            Map<String, String> m = new LinkedHashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                m.put(headers.get(i), row.get(i));
            }
            out.add(m);
        }
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table)) return false;
        Table other = (Table) o;
        return headers.equals(other.headers) && rows.equals(other.rows);
    }

    @Override
    public int hashCode() {
        return 31 * headers.hashCode() + rows.hashCode();
    }

    @Override
    public String toString() {
        return "<Table " + rows.size() + " filas x " + headers.size() + " columnas>";
    }
}
