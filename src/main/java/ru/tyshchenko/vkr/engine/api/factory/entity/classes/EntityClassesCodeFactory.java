package ru.tyshchenko.vkr.engine.api.factory.entity.classes;

import ru.tyshchenko.vkr.engine.api.factory.entity.EntityCodeFactory;

import java.util.Map;

public interface EntityClassesCodeFactory extends EntityCodeFactory<Map<String, String>> {

    String language();
}
