package com.csvlib.io;

import com.csvlib.core.IReader;
import com.csvlib.core.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Lector CSV simple sobre texto plano. */
public class CsvReader implements IReader {
    @Override
    public Table read(String source) {
        if (source == null || source.trim().isEmpty()) {
            return new Table(new ArrayList<>(), new ArrayList<>());
        }

        List<String> lines = new ArrayList<>();
        for (String rawLine : source.split("\\R")) {
            if (!rawLine.trim().isEmpty()) {
                lines.add(rawLine);
            }
        }

        if (lines.isEmpty()) {
            return new Table(new ArrayList<>(), new ArrayList<>());
        }

        List<String> headers = parseLine(lines.get(0));
        List<List<String>> rows = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            rows.add(parseLine(lines.get(i)));
        }

        return new Table(headers, rows);
    }

    protected List<String> parseLine(String line) {
        return new ArrayList<>(Arrays.asList(line.split(",", -1)));
    }
}
