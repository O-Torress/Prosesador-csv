package com.csvlib.core;

/**
 * Contrato para una transformacion sobre una tabla.
 *
 * Base del Principio Abierto/Cerrado: se pueden crear nuevas operaciones
 * (nuevas clases que implementen esta interfaz) sin modificar el motor que las
 * ejecuta. Tambien es la base de los patrones Command y Strategy.
 */
public interface IOperation {
    /** Recibe una tabla y devuelve una tabla transformada. */
    Table apply(Table table);
}
