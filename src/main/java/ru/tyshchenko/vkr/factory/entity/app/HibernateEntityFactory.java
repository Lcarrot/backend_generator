package ru.tyshchenko.vkr.factory.entity.app;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.tyshchenko.vkr.entity.Column;
import ru.tyshchenko.vkr.entity.EntityInfo;
import ru.tyshchenko.vkr.entity.java.ColumnTypeMapper;
import ru.tyshchenko.vkr.entity.types.ConstraintType;
import ru.tyshchenko.vkr.factory.entity.EntityFactory;
import ru.tyshchenko.vkr.util.StringUtils;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.tyshchenko.vkr.util.StringUtils.*;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "entity", name = "generator", havingValue = AppDialect.HIBERNATE_JAVA)
public class HibernateEntityFactory implements AppEntityFactory {

    private final ColumnTypeMapper columnTypeMapper;

    private String entityPattern;

    @PostConstruct
    @SneakyThrows
    public void init() {
        entityPattern = Files.readString(
                ResourceUtils.getFile("classpath:patterns/Entity.pattern").toPath());
    }

    @Override
    public List<String> buildEntities(List<EntityInfo> entityInfos) {
        List<String> entities = new ArrayList<>();
        for (EntityInfo entity : entityInfos) {
            Set<String> imports = new HashSet<>();
            StringBuilder entityBuilder = new StringBuilder(entityPattern);
            replaceIdField(entityBuilder, entity.getPrimaryKey());
            replaceEntityName(entityBuilder, entity.getEntityName());
            String fields = buildDefaultFields(entity.getColumns(), imports);
            replaceFields(entityBuilder, fields);
            StringBuilder refBuilder = new StringBuilder();
            for (EntityInfo entityTo : entity.getEntityTo()) {
                refBuilder.append(addManyToOneField(entityTo.getEntityName()));
            }
            for (EntityInfo entityFrom : entity.getEntityFrom()) {
                refBuilder.append(addOneToManyField(entity.getEntityName(), entityFrom.getEntityName()));
            }
            replaceRefers(entityBuilder, refBuilder.toString());
            replaceImports(entityBuilder, imports);
            entities.add(entityBuilder.toString());
        }
        return entities;
    }

    private void replaceIdField(StringBuilder builder, String idColumnName) {
        String pattern = "${idName}";
        int index = builder.indexOf(pattern);
        builder.replace(index, index + pattern.length(), toCamelCase(idColumnName));
    }

    private void replaceRefers(StringBuilder builder, String refers) {
        String pattern = "${entity_references}";
        int start = builder.indexOf(pattern);
        builder.replace(start, start + pattern.length(), refers);
    }

    private String addOneToManyField(String mappedBy, String toRef) {
        return TAB + "@OneToMany(mappedBy=\"" + mappedBy + "\")\n" +
                TAB + "private List<" + toUpperCaseFirstLetter(toCamelCase(toRef)) + "> " + toCamelCase(toRef) + "s;\n";
    }

    private String addManyToOneField(String mappedBy) {
        return TAB + "@ManyToOne" + "\n" +
                TAB + "@JoinColumn(name=\"" + mappedBy + "_id\")\n" +
                TAB + "private " + toUpperCaseFirstLetter(toCamelCase(mappedBy)) + " " + toCamelCase(mappedBy) + "s;\n";
    }

    private void replaceFields(StringBuilder builder, String fields) {
        String pattern = "${entity_fields}";
        int index = builder.indexOf(pattern);
        builder.replace(index, index + pattern.length(), fields);
    }

    private void replaceImports(StringBuilder builder, Set<String> imports) {
        StringBuilder importString = new StringBuilder();
        for (String imp : imports) {
            importString.append("import ").append(imp).append(";\n");
        }
        String pattern = "${imports}";
        int startIndex = builder.indexOf(pattern);
        builder.replace(startIndex, startIndex + pattern.length(), importString.toString());
    }

    private void replaceEntityName(StringBuilder builder, String entityName) {
        String pattern = "${entity_name}";
        int start = builder.indexOf(pattern);
        builder.replace(start, start + pattern.length(), toUpperCaseFirstLetter(toCamelCase(entityName)));
    }

    private String buildDefaultFields(List<Column> columns, Set<String> imports) {
        StringBuilder builder = new StringBuilder();
        for (Column column : columns) {
            appendField(builder, column, imports);
        }
        return builder.toString();
    }

    private void appendField(StringBuilder builder, Column column, Set<String> imports) {
        if (column.getConstraintTypes().contains(ConstraintType.NOT_NULL)) {
            imports.add("javax.validation.constraints.NotNull");
            builder.append(TAB).append("@NotNull").append("\n");
        }
        builder.append(TAB).append("private ")
                .append(columnTypeMapper.mapType(column.getType())).append(" ")
                .append(toCamelCase(column.getName())).append(";\n");
    }
}
