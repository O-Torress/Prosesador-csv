package com.csvlib.operations;

import com.csvlib.core.IOperation;
import com.csvlib.core.Table;

import java.util.ArrayList;
import java.util.List;

/** Implementaciones concretas de operaciones reutilizables sobre Table. */
public final class Operations {
    private Operations() {
    }

    public static class AddRow implements IOperation {
        private final List<String> row;
        private final Integer index;

        public AddRow(List<String> row) {
            this(row, null);
        }

        public AddRow(List<String> row, Integer index) {
            this.row = row;
            this.index = index;
        }

        @Override
        public Table apply(Table table) {
            return (index == null) ? table.addRow(row) : table.addRow(row, index);
        }
    }

    public static class RemoveRow implements IOperation {
        private final int index;

        public RemoveRow(int index) {
            this.index = index;
        }

        @Override
        public Table apply(Table table) {
            return table.removeRow(index);
        }
    }

    public static class AddColumn implements IOperation {
        private final String name;
        private final List<String> values;
        private final String defaultValue;

        public AddColumn(String name, String defaultValue) {
            this(name, null, defaultValue);
        }

        public AddColumn(String name, List<String> values, String defaultValue) {
            this.name = name;
            this.values = values;
            this.defaultValue = defaultValue;
        }

        @Override
        public Table apply(Table table) {
            return table.addColumn(name, values, defaultValue);
        }
    }

    public static class RemoveColumn implements IOperation {
        private final String name;

        public RemoveColumn(String name) {
            this.name = name;
        }

        @Override
        public Table apply(Table table) {
            return table.removeColumn(name);
        }
    }

    public static class SetCell implements IOperation {
        private final int rowIndex;
        private final int columnIndex;
        private final String value;

        public SetCell(int rowIndex, int columnIndex, String value) {
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.value = value;
        }

        @Override
        public Table apply(Table table) {
            if (rowIndex < 0 || rowIndex >= table.rowCount()) {
                throw new IndexOutOfBoundsException("Indice de fila fuera de rango: " + rowIndex);
            }
            if (columnIndex < 0 || columnIndex >= table.columnCount()) {
                throw new IndexOutOfBoundsException("Indice de columna fuera de rango: " + columnIndex);
            }

            List<List<String>> rows = table.getRows();
            List<String> row = new ArrayList<>(rows.get(rowIndex));
            row.set(columnIndex, String.valueOf(value));
            rows.set(rowIndex, row);
            return new Table(table.getHeaders(), rows);
        }
    }
}
