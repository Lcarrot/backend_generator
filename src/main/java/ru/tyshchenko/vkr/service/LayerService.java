package ru.tyshchenko.vkr.service;

import java.util.List;

public interface LayerService<T, E> {

    void save(List<T> source);

    List<E> getApi();
}
