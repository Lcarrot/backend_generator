package ru.tyshchenko.vkr.factory.entity;

import ru.tyshchenko.vkr.entity.EntityInfo;

import java.util.List;

public interface EntityFactory<T> {

    T buildEntities(List<EntityInfo> entityInfos);
}
