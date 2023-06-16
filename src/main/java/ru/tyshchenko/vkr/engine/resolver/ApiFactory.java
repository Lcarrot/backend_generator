package ru.tyshchenko.vkr.engine.resolver;

import java.util.List;

public interface ApiFactory<T, F> {

    List<T> buildApi(List<F> metaInfo);
}