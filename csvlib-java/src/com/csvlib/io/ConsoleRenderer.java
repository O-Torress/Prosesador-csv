package com.csvlib.io;

import com.csvlib.core.IRenderer;
import com.csvlib.core.Table;

import java.util.List;

/** Renderer por defecto para consola. */
public class ConsoleRenderer implements IRenderer {
    @Override
    public String render(Table table) {
        List<String> headers = table.getHeaders();
        List<List<String>> rows = table.getRows();

        StringBuilder sb = new StringBuilder();
        sb.append(String.join(" | ", headers));
        sb.append(System.lineSeparator());

        for (List<String> row : rows) {
            sb.append(String.join(" | ", row));
            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
