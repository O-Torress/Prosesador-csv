package com.csvlib.core;

/** Contrato para representar visualmente una tabla (impresion). */
public interface IRenderer {
    String render(Table table);
}
