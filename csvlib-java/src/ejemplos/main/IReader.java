package com.csvlib.core;

/**
 * Contrato para cualquier fuente capaz de producir una tabla.
 *
 * ISP (Segregacion de Interfaces): expone unicamente la responsabilidad de leer.
 * DIP (Inversion de Dependencias): los modulos de alto nivel dependen de esta
 * abstraccion, no de una implementacion concreta.
 */
public interface IReader {
    /** Lee la fuente indicada y devuelve un objeto Table (datos como String). */
    Table read(String source);
}
