package ru.tyshchenko.vkr.engine.api.factory;

public interface CodeFactory<T, F> {

    F generate(T infos);
}
