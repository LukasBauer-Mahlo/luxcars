package de.luxcars.backend.database.function;

@FunctionalInterface
public interface ThrowableConsumer<I, T extends Throwable> {

  void accept(I i) throws T;

}
