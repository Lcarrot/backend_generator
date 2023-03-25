package ru.tyshchenko.vkr.factory.entity;

import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.entity.source.Entity;
import ru.tyshchenko.vkr.entity.EntityInfo;
import ru.tyshchenko.vkr.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.entity.types.ReferenceType;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Component
public class EntityInfoMetaInfoFactory {

    public List<EntityInfo> getMetaInfo(List<Entity> entityInfos, List<ReferenceColumn> referenceColumns) {
        Map<String, EntityInfo> entityInfoMap = entityInfos.stream().map(entity ->
                EntityInfo.builder()
                        .entityName(entity.getEntityName())
                        .columns(entity.getColumns())
                        .primaryKey(entity.getPrimaryKey())
                        .build()
        ).collect(toMap(EntityInfo::getEntityName, Function.identity()));
        for (ReferenceColumn referenceColumn : referenceColumns) {
            if (referenceColumn.getReferenceType() == ReferenceType.ONE_TO_MANY) {
                entityInfoMap.get(referenceColumn.getEntityTo()).getEntityFrom()
                        .add(entityInfoMap.get(referenceColumn.getEntityFrom()));
                entityInfoMap.get(referenceColumn.getEntityFrom()).getEntityTo()
                        .add(entityInfoMap.get(referenceColumn.getEntityTo()));
            }
        }
        return new ArrayList<>(entityInfoMap.values());
    }
}
