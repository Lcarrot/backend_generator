package ru.tyshchenko.vkr.engine.api.models.entity.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityInfo {

    private String entityName;
    private String primaryKey;
    private List<Column> columns;
    @Builder.Default
    private List<EntityInfo> entityTo = new ArrayList<>();
    @Builder.Default
    private List<EntityInfo> entityFrom = new ArrayList<>();
}