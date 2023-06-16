package ru.tyshchenko.vkr.engine.api.factory.entity.sql;

import ru.tyshchenko.vkr.engine.api.factory.DefaultPlaceholder;

public class SqlDefaultPlaceholder extends DefaultPlaceholder {

    public static String TABLE_NAME = "${table_name}";
    public static String REFERENCE_TABLE = "${reference_table}";
    public static String REFERENCE_COLUMN = "${reference_column}";
}
