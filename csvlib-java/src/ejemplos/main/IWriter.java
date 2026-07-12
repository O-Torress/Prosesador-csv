package com.csvlib.core;

/** Contrato para cualquier destino capaz de persistir una tabla. */
public interface IWriter {
    void write(Table table, String target);
}
