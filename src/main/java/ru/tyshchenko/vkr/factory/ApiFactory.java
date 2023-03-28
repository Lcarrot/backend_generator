package ru.tyshchenko.vkr.factory;

import java.util.List;

public interface ApiFactory<T, F> {

    List<T> buildApi(List<F> metaInfo);
}