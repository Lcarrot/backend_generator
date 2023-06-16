package ru.tyshchenko.vkr.engine.resolver.type.mapper;

import ru.tyshchenko.vkr.engine.api.models.entity.types.ColumnType;

public interface ColumnTypeMapper {

    Type mapType(ColumnType type);

    String language();
}
