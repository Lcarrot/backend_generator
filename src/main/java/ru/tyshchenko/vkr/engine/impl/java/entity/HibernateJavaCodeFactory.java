package ru.tyshchenko.vkr.engine.impl.java.entity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.Column;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.models.entity.types.ConstraintType;
import ru.tyshchenko.vkr.engine.api.factory.entity.classes.EntityClassesCodeFactory;
import ru.tyshchenko.vkr.engine.impl.SupportedORMLanguage;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.JavaColumnTypeMapper;

import java.util.*;
import java.util.stream.Stream;

import static ru.tyshchenko.vkr.engine.api.factory.DefaultPlaceholder.*;
import static ru.tyshchenko.vkr.engine.api.factory.entity.classes.EntityClassPlaceholder.ID_FIElD;
import static ru.tyshchenko.vkr.engine.api.factory.entity.sql.SqlDefaultPlaceholder.TABLE_NAME;
import static ru.tyshchenko.vkr.engine.util.PatternUtils.buildImports;
import static ru.tyshchenko.vkr.engine.util.PatternUtils.getPatternFromResource;
import static ru.tyshchenko.vkr.engine.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class HibernateJavaCodeFactory implements EntityClassesCodeFactory {


    private final JavaColumnTypeMapper javaColumnTypeMapper;

    private String entity;
    private String field;
    private String oneToManyReference;
    private String manyToOneReference;

    @PostConstruct
    void init() {
        String path = "patterns/entity/classes/";
        entity = getPatternFromResource(path + "Entity.pattern");
        field = getPatternFromResource(path + "Field.pattern");
        oneToManyReference = getPatternFromResource(path + "OneToManyField.pattern");
        manyToOneReference = getPatternFromResource(path + "ManyToOneField.pattern");
    }

    @Override
    public Map<String, String> generate(List<EntityInfo> infos) {
        Map<String, String> entities = new HashMap<>();
        for (EntityInfo entity : infos) {
            Set<String> imports = new HashSet<>();
            StringBuilder entityBuilder = new StringBuilder(this.entity);
            replaceByRegex(entityBuilder, ID_FIElD, toCamelCase(entity.getPrimaryKey()));
            replaceByRegex(entityBuilder, CLASS_NAME, toClassName(entity.getEntityName()));
            replaceByRegex(entityBuilder, FIELDS, buildFields(entity.getColumns(), imports));

            replaceByRegex(entityBuilder, "${entity_references}",buildRefs(entity, imports));
            replaceByRegex(entityBuilder, IMPORTS, buildImports(imports));
            entities.put(entity.getEntityName(), entityBuilder.toString());
        }
        return entities;
    }

    private String buildFields(List<Column> columns, Set<String> imports) {
        return String.join("\n", columns.stream().map(column -> buildField(column, imports)).toList());
    }

    private String buildField(Column column, Set<String> imports) {
        StringBuilder fieldBuilder = new StringBuilder(field);
        if (column.getConstraintTypes().contains(ConstraintType.NOT_NULL)) {
            imports.add("javax.validation.constraints.NotNull");
            replaceByRegex(fieldBuilder, CONSTRAINTS, "@NotNull");
        } else {
            replaceByRegex(fieldBuilder, CONSTRAINTS, "");
        }
        var type = javaColumnTypeMapper.mapType(column.getType());
        if (type.packet() != null) {
            imports.add(type.packet() + "." + type.type());
        }
        replaceByRegex(fieldBuilder, FIELD_TYPE, type.type());
        replaceByRegex(fieldBuilder, FIELD_NAME, column.getName());
        return fieldBuilder.toString();
    }

    private String buildRefs(EntityInfo entityInfo, Set<String> imports) {
        return String.join("\n",
                Stream.of(
                                entityInfo.getEntityFrom().stream()
                                        .map(info -> buildOneToManyField(entityInfo.getEntityName(), info.getEntityName(), imports))
                                        .toList(),
                                entityInfo.getEntityTo().stream().map(info -> buildManyToOneField(info.getEntityName()))
                                        .toList())
                        .flatMap(Collection::stream).toList());
    }

    private String buildOneToManyField(String entityName, String refEntity, Set<String> imports) {
        imports.add("java.util.List");
        var refBuilder = new StringBuilder(oneToManyReference);
        replaceByRegex(refBuilder, TABLE_NAME, entityName);
        replaceByRegex(refBuilder, FIELD_TYPE, toClassName(refEntity));
        replaceByRegex(refBuilder, FIELD_NAME, toCamelCase(refEntity));
        return refBuilder.toString();
    }

    private String buildManyToOneField(String entityName) {
        var refBuilder = new StringBuilder(manyToOneReference);
        replaceByRegex(refBuilder, ID_FIElD, entityName + "_id");
        replaceByRegex(refBuilder, FIELD_TYPE, toClassName(entityName));
        replaceByRegex(refBuilder, FIELD_NAME, toCamelCase(entityName));
        return refBuilder.toString();
    }

    @Override
    public String language() {
        return SupportedORMLanguage.JAVA_HIBERNATE.name();
    }
}