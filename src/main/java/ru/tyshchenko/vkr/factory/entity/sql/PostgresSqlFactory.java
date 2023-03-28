package ru.tyshchenko.vkr.factory.entity.sql;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.dto.entity.meta.Column;
import ru.tyshchenko.vkr.dto.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.dto.entity.types.ColumnType;
import ru.tyshchenko.vkr.dto.entity.types.ConstraintType;

import java.util.List;

import static ru.tyshchenko.vkr.util.StringUtils.replaceByRegex;

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
        replaceByRegex(entityBuilder, "${table_name}", entity.getEntityName());
        String fields = buildFields(entity.getPrimaryKey(), entity.getColumns());
        replaceByRegex(entityBuilder, "${fields}", fields);
        return entityBuilder.toString();
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