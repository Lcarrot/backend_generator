package ru.tyshchenko.vkr.engine.api.factory.entity;

import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.factory.CodeFactory;

import java.util.List;

public interface EntityCodeFactory<F> extends CodeFactory<List<EntityInfo>, F> {
}
