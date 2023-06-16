package ru.tyshchenko.vkr.engine.impl.sql.postgres;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.Column;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.models.entity.types.ColumnType;
import ru.tyshchenko.vkr.engine.api.models.entity.types.ConstraintType;
import ru.tyshchenko.vkr.engine.api.factory.DefaultPlaceholder;
import ru.tyshchenko.vkr.engine.api.factory.entity.sql.DefaultSqlCodeFactory;
import ru.tyshchenko.vkr.engine.api.factory.entity.sql.SqlDefaultPlaceholder;
import ru.tyshchenko.vkr.engine.impl.SupportedDbLanguage;

import java.util.ArrayList;
import java.util.List;

import static ru.tyshchenko.vkr.util.PatternUtils.getPatternFromResource;
import static ru.tyshchenko.vkr.util.StringUtils.replaceByRegex;

@Component
public class PostgresqlCodeFactory extends DefaultSqlCodeFactory {

    private String table;
    private String reference;
    private String field;

    @PostConstruct
    public void init() {
        table = getPatternFromResource("patterns/entity/sql/Table.pattern");
        reference = getPatternFromResource("patterns/entity/sql/Reference.pattern");
        field = getPatternFromResource("patterns/entity/sql/Field.pattern");
    }

    protected String buildEntity(EntityInfo entity) {
        StringBuilder entityBuilder = new StringBuilder(table);
        replaceByRegex(entityBuilder, SqlDefaultPlaceholder.TABLE_NAME, entity.getEntityName());
        String fields = buildFields(entity.getPrimaryKey(), entity.getColumns());
        replaceByRegex(entityBuilder, SqlDefaultPlaceholder.FIELDS, fields);
        return entityBuilder.toString();
    }

    private String buildFields(String primaryKey, List<Column> columns) {
        List<String> fields = new ArrayList<>();
        fields.add(buildPrimaryKey(primaryKey));
        for (Column column : columns) {
            fields.add(buildField(column));
        }
        return String.join(", ", fields);
    }

    private String buildPrimaryKey(String primaryKey) {
        StringBuilder builder = new StringBuilder(field);
        replaceByRegex(builder, DefaultPlaceholder.FIELD_NAME, primaryKey);
        replaceByRegex(builder, DefaultPlaceholder.FIELD_TYPE, ColumnType.BIGSERIAL.name());
        replaceByRegex(builder, DefaultPlaceholder.CONSTRAINTS, "CONSTRAINT prkey PRIMARY KEY");
        return builder.toString();
    }

    private String buildField(Column column) {
        StringBuilder builder = new StringBuilder(field);
        replaceByRegex(builder, DefaultPlaceholder.FIELD_NAME, column.getName());
        replaceByRegex(builder, DefaultPlaceholder.FIELD_TYPE, column.getType().name());
        StringBuilder constraintBuilder = new StringBuilder();
        for (ConstraintType constraint : column.getConstraintTypes()) {
            constraintBuilder.append(" ").append(constraint.name());
        }
        replaceByRegex(builder, DefaultPlaceholder.CONSTRAINTS, constraintBuilder.toString());
        return builder.toString();
    }

    @Override
    protected String buildReference(EntityInfo entityInfo) {
        StringBuilder referenceBuilder = new StringBuilder();
        for (EntityInfo entityTo : entityInfo.getEntityTo()) {
            referenceBuilder.append(buildOneToMany(entityInfo, entityTo)).append("\n");
        }
        return referenceBuilder.toString();
    }

    private String buildOneToMany(EntityInfo entity, EntityInfo entityTo) {
        StringBuilder referenceBuilder = new StringBuilder(reference);
        replaceByRegex(referenceBuilder, SqlDefaultPlaceholder.TABLE_NAME, entity.getEntityName());
        replaceByRegex(referenceBuilder, DefaultPlaceholder.FIELD_NAME, entityTo.getEntityName() + "_id");
        replaceByRegex(referenceBuilder, DefaultPlaceholder.FIELD_TYPE, ColumnType.BIGINT.name());
        replaceByRegex(referenceBuilder, SqlDefaultPlaceholder.REFERENCE_TABLE, entityTo.getEntityName());
        replaceByRegex(referenceBuilder, SqlDefaultPlaceholder.REFERENCE_COLUMN, entityTo.getPrimaryKey());
        return referenceBuilder.toString();
    }

    @Override
    public String dialect() {
        return SupportedDbLanguage.POSTGRESQL.name();
    }
}
