package de.luxcars.backend.database.function;

@FunctionalInterface
public interface ThrowableFunction<I, O, T extends Throwable> {

  O apply(I i) throws T;

}
