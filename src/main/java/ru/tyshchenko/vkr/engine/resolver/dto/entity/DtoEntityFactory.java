package ru.tyshchenko.vkr.engine.resolver.dto.entity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.engine.api.models.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.Column;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.factory.DefaultPlaceholder;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.ColumnTypeMapper;
import ru.tyshchenko.vkr.engine.resolver.type.mapper.Type;

import java.nio.file.Files;
import java.util.*;

import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class DtoEntityFactory {

    private final List<ColumnTypeMapper> columnTypeMappers;

    private String dtoPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        dtoPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/EntityDTO.pattern").toPath());
    }

    public Map<String, String> buildDto(List<EntityDtoInfo> dtoInfos, Map<String, EntityInfo> entityInfoMap, String language) {
        Map<String, String> entities = new HashMap<>();
        for (var dto : dtoInfos) {
            var imports = new HashSet<String>();
            var entity = entityInfoMap.get(dto.getEntityName());
            StringBuilder entityBuilder = new StringBuilder(dtoPattern);
            replaceByRegex(entityBuilder, DefaultPlaceholder.ENTITY,
                    toUpperCaseFirstLetter(toCamelCase(dto.getEntityName())));
            String fields = buildDefaultFields(entity.getColumns(), imports, language);
            replaceByRegex(entityBuilder, DefaultPlaceholder.FIELDS, fields);
            replaceByRegex(entityBuilder, "${build_methods}", buildTo(entity.getColumns()));
            replaceImports(entityBuilder, imports);
            entities.put(entity.getEntityName(), entityBuilder.toString());
        }
        return entities;
    }

    private void replaceImports(StringBuilder builder, Set<String> imports) {
        StringBuilder importString = new StringBuilder();
        for (String imp : imports) {
            importString.append("import ").append(imp).append(";\n");
        }
        replaceByRegex(builder, DefaultPlaceholder.IMPORTS, importString.toString());
    }

    private String buildDefaultFields(List<Column> columns, Set<String> imports, String language) {
        StringBuilder builder = new StringBuilder();
        for (Column column : columns) {
            appendField(builder, column, imports, language);
        }
        return builder.toString();
    }

    private String buildTo(List<Column> columns) {
        StringBuilder toBuilder = new StringBuilder();
        for (var col: columns) {
            toBuilder.append(TAB.repeat(3))
                    .append(".").append(toCamelCase(col.getName()))
                    .append("(entity.get").append(toUpperCaseFirstLetter(toCamelCase(col.getName())))
                    .append(")")
                    .append("\n");
        }
        toBuilder.deleteCharAt(toBuilder.length() - 1);
        return toBuilder.toString();
    }

    private void appendField(StringBuilder builder, Column column, Set<String> imports, String language) {
        Type type = columnTypeMappers.stream()
                .filter(mapper -> language.contains(mapper.language())).findFirst().orElseThrow()
                .mapType(column.getType());
        if (type.packet() != null) {
            imports.add(type.packet() + "." + type.type());
        }
        builder.append(TAB).append("private ")
                .append(type.type()).append(" ")
                .append(toCamelCase(column.getName())).append(";\n");
    }
}