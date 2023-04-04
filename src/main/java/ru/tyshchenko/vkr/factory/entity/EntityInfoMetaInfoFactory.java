package ru.tyshchenko.vkr.factory.entity;

import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.dto.entity.meta.Column;
import ru.tyshchenko.vkr.dto.entity.source.Entity;
import ru.tyshchenko.vkr.dto.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.dto.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.dto.entity.types.ColumnType;
import ru.tyshchenko.vkr.dto.entity.types.ConstraintType;
import ru.tyshchenko.vkr.dto.entity.types.ReferenceType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
public class EntityInfoMetaInfoFactory {

    public Map<String, EntityInfo> getMetaInfo(List<Entity> entityInfos, List<ReferenceColumn> referenceColumns) {
        Map<String, EntityInfo> entityInfoMap = entityInfos.stream().map(entity ->
                EntityInfo.builder()
                        .entityName(entity.getEntityName())
                        .columns(entity.getColumns().stream().map(columnSource -> {
                            Set<ConstraintType> constraintTypes = new HashSet<>();
                            if (columnSource.isUnique()) {
                                constraintTypes.add(ConstraintType.UNIQUE);
                            }
                            if (columnSource.isNotNull()) {
                                constraintTypes.add(ConstraintType.NOT_NULL);
                            }
                            return Column.builder()
                                    .type(ColumnType.valueOf(columnSource.getType()))
                                    .name(columnSource.getName())
                                    .constraintTypes(constraintTypes)
                                    .build();
                        }).collect(Collectors.toList()))
                        .primaryKey(entity.getPrimaryKey())
                        .build()
        ).collect(toMap(EntityInfo::getEntityName, Function.identity()));
        for (ReferenceColumn referenceColumn : referenceColumns) {
            if (ReferenceType.valueOf(referenceColumn.getReferenceType().toUpperCase()) == ReferenceType.ONE_TO_MANY) {
                entityInfoMap.get(referenceColumn.getEntityTo()).getEntityFrom()
                        .add(entityInfoMap.get(referenceColumn.getEntityFrom()));
                entityInfoMap.get(referenceColumn.getEntityFrom()).getEntityTo()
                        .add(entityInfoMap.get(referenceColumn.getEntityTo()));
            }
        }
        return entityInfoMap;
    }
}
