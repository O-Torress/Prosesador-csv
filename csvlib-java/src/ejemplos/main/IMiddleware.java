package com.csvlib.core;

import java.util.function.Function;

/**
 * Contrato para interceptar/modificar el resultado de las operaciones.
 *
 * Permite al usuario extender o modificar el resultado final de las funciones
 * sin tocar el codigo base (patron Decorator / cadena de responsabilidad).
 */
public interface IMiddleware {
    /** next es una funcion que continua la cadena de ejecucion. */
    Table process(Table table, Function<Table, Table> next);
}
