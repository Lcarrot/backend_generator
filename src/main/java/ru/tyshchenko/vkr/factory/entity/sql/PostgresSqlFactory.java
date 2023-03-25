package ru.tyshchenko.vkr.factory.entity.sql;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.entity.Column;
import ru.tyshchenko.vkr.entity.EntityInfo;
import ru.tyshchenko.vkr.entity.types.ColumnType;
import ru.tyshchenko.vkr.entity.types.ConstraintType;
import ru.tyshchenko.vkr.factory.entity.EntityFactory;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "sql", name = "generator", havingValue = SqlDialect.POSTGRESQL)
public class PostgresSqlFactory implements SqlEntityFactory {

    private final static String TABLE_PATTERN = "CREATE TABLE ${table_name} (${fields});";

    @Override
    public String buildEntities(List<EntityInfo> entityInfos) {
        StringBuilder changeSetBuilder = new StringBuilder();
        for (EntityInfo entity : entityInfos) {
            changeSetBuilder.append(buildEntity(entity)).append("\n");
            for (EntityInfo entityTo : entity.getEntityTo()) {
                changeSetBuilder.append(buildOneToManyConstraint(entity.getEntityName(),
                        entityTo.getEntityName(), entityTo.getPrimaryKey())).append("\n");
            }
        }
        return changeSetBuilder.toString();
    }

    private String buildEntity(EntityInfo entity) {
        StringBuilder entityBuilder = new StringBuilder(TABLE_PATTERN);
        replaceEntityName(entityBuilder, entity.getEntityName());
        String fields = buildFields(entity.getPrimaryKey(), entity.getColumns());
        replaceFields(entityBuilder, fields);
        return entityBuilder.toString();
    }

    private void replaceEntityName(StringBuilder stringBuilder, String entityName) {
        String tablePattern = "${table_name}";
        int startIndex = stringBuilder.indexOf(tablePattern);
        stringBuilder.replace(startIndex, startIndex + tablePattern.length(), entityName);
    }

    private void replaceFields(StringBuilder builder, String fields) {
        String fieldPattern = "${fields}";
        int startIndex = builder.indexOf(fieldPattern);
        builder.replace(startIndex, startIndex + fieldPattern.length(), fields);
    }

    private String buildFields(String primaryKey, List<Column> columns) {
        StringBuilder columnBuilder = new StringBuilder();
        columnBuilder.append(primaryKey).append(" bigserial CONSTRAINT prkey PRIMARY KEY,");
        for (Column column : columns) {
            columnBuilder.append(column.getName()).append(" ").append(column.getType().name());
            for (ConstraintType constraint : column.getConstraintTypes()) {
                columnBuilder.append(" ").append(constraint.name());
            }
            columnBuilder.append(",");
        }
        columnBuilder.deleteCharAt(columnBuilder.length() - 1);
        return columnBuilder.toString();
    }

    private String buildOneToManyConstraint(String entityFrom, String entityTo, String idColumnEntityTo) {
        return "ALTER TABLE " +
                entityFrom +
                " " +
                "ADD COLUMN " +
                entityTo + "_id" +
                " " +
                ColumnType.BIGINT.name() +
                " " +
                "REFERENCES " +
                entityTo +
                "(" +
                idColumnEntityTo +
                ");";
    }
}