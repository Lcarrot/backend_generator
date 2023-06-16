package ru.tyshchenko.vkr.engine.api.factory.entity.sql;

import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;

import java.util.List;

public abstract class DefaultSqlCodeFactory implements SqlCodeFactory {

    @Override
    public String generate(List<EntityInfo> infos) {
        StringBuilder changeSetBuilder = new StringBuilder();
        for (EntityInfo entity : infos) {
            changeSetBuilder.append(buildEntity(entity)).append("\n");
            changeSetBuilder.append(buildReference(entity)).append("\n");
        }
        return changeSetBuilder.toString();
    }

    protected abstract String buildEntity(EntityInfo entityInfo);
    protected abstract String buildReference(EntityInfo entityInfo);
}
