package ru.tyshchenko.vkr.engine.api.factory.entity.sql;

import ru.tyshchenko.vkr.engine.api.factory.entity.EntityCodeFactory;

public interface SqlCodeFactory extends EntityCodeFactory<String> {

    String dialect();
}
