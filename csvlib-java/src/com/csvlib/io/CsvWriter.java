package com.csvlib.io;

import com.csvlib.core.IWriter;
import com.csvlib.core.Table;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Escritor CSV simple hacia un archivo. */
public class CsvWriter implements IWriter {
    @Override
    public void write(Table table, String target) {
        if (target == null || target.isBlank()) {
            throw new IllegalArgumentException("El destino no puede ser nulo o vacío");
        }

        StringBuilder sb = new StringBuilder();
        List<String> headers = table.getHeaders();
        if (!headers.isEmpty()) {
            sb.append(String.join(",", headers));
            sb.append(System.lineSeparator());
        }

        for (List<String> row : table.getRows()) {
            sb.append(String.join(",", row));
            sb.append(System.lineSeparator());
        }

        Path targetPath = Path.of(target);
        Path parent = targetPath.getParent();
        if (parent != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el directorio de salida: " + parent, e);
            }
        }

        try {
            Files.writeString(targetPath, sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar la tabla en: " + target, e);
        }
    }
}
