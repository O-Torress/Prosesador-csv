package com.csvlib.io;

import com.csvlib.core.IRenderer;
import com.csvlib.core.Table;

import java.util.List;

/** Renderer para mostrar la tabla en formato Markdown. */
public class MarkdownRenderer implements IRenderer {
    @Override
    public String render(Table table) {
        List<String> headers = table.getHeaders();
        List<List<String>> rows = table.getRows();

        StringBuilder sb = new StringBuilder();
        sb.append("| ").append(String.join(" | ", headers)).append(" |");
        sb.append(System.lineSeparator());
        sb.append("| ");
        for (int i = 0; i < headers.size(); i++) {
            sb.append("---");
            if (i < headers.size() - 1) {
                sb.append(" | ");
            }
        }
        sb.append(" |");
        sb.append(System.lineSeparator());

        for (List<String> row : rows) {
            sb.append("| ").append(String.join(" | ", row)).append(" |");
            sb.append(System.lineSeparator());
        }

        return sb.toString().trim();
    }
}
