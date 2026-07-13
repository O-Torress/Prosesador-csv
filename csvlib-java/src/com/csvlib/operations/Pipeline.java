package com.csvlib.operations;

import com.csvlib.core.IMiddleware;
import com.csvlib.core.IOperation;
import com.csvlib.core.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** Motor de ejecucion de operaciones con soporte para middleware. */
public class Pipeline {
    private final List<IOperation> operations = new ArrayList<>();
    private final List<IMiddleware> middlewares = new ArrayList<>();

    public void add(IOperation operation) {
        operations.add(operation);
    }

    public void use(IMiddleware middleware) {
        middlewares.add(middleware);
    }

    public Table run(Table input) {
        Table current = (input != null) ? input : new Table(new ArrayList<>(), new ArrayList<>());
        Function<Table, Table> chain = table -> {
            for (IOperation operation : operations) {
                table = operation.apply(table);
            }
            return table;
        };

        for (int i = middlewares.size() - 1; i >= 0; i--) {
            final IMiddleware middleware = middlewares.get(i);
            final Function<Table, Table> prev = chain;
            chain = table -> middleware.process(table, prev);
        }

        return chain.apply(current);
    }
}