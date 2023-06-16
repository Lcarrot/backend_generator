package ru.tyshchenko.vkr.engine.api.models.entity.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity {

    private String entityName;
    private String primaryKey;
    private List<ColumnSource> columns;
}
