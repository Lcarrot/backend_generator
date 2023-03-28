package ru.tyshchenko.vkr.factory.dto.entity;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.dto.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.dto.entity.meta.Column;
import ru.tyshchenko.vkr.dto.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.dto.entity.java.ColumnTypeMapper;
import ru.tyshchenko.vkr.util.PatternUtils;

import java.nio.file.Files;
import java.util.*;

import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
public class DtoFactory {

    private final ColumnTypeMapper columnTypeMapper;

    private String dtoPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        dtoPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/EntityDTO.pattern").toPath());
    }

    public List<String> buildDto(List<EntityDtoInfo> dtoInfos, Map<String, EntityInfo> entityInfoMap) {
        List<String> entities = new ArrayList<>();
        for (var dto : dtoInfos) {
            var imports = new HashSet<String>();
            var entity = entityInfoMap.get(dto.getEntityName());
            StringBuilder entityBuilder = new StringBuilder(dtoPattern);
            replaceByRegex(entityBuilder, PatternUtils.ENTITY,
                    toUpperCaseFirstLetter(toCamelCase(dto.getEntityName())));
            String fields = buildDefaultFields(entity.getColumns(), imports);
            replaceByRegex(entityBuilder, PatternUtils.FIELDS, fields);
            replaceByRegex(entityBuilder, "${build_methods}", buildTo(entity.getColumns()));
            replaceImports(entityBuilder, imports);
            entities.add(entityBuilder.toString());
        }
        return entities;
    }

    private void replaceImports(StringBuilder builder, Set<String> imports) {
        StringBuilder importString = new StringBuilder();
        for (String imp : imports) {
            importString.append("import ").append(imp).append(";\n");
        }
        replaceByRegex(builder, PatternUtils.IMPORTS, importString.toString());
    }

    private String buildDefaultFields(List<Column> columns, Set<String> imports) {
        StringBuilder builder = new StringBuilder();
        for (Column column : columns) {
            appendField(builder, column, imports);
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

    private void appendField(StringBuilder builder, Column column, Set<String> imports) {
        ColumnTypeMapper.Type type = columnTypeMapper.mapType(column.getType());
        if (type.packet() != null) {
            imports.add(type.packet() + "." + type.type());
        }
        builder.append(TAB).append("private ")
                .append(type.type()).append(" ")
                .append(toCamelCase(column.getName())).append(";\n");
    }
}