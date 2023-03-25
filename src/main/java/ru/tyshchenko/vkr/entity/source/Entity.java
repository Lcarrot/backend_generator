package ru.tyshchenko.vkr.entity.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.entity.Column;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity {

    private String entityName;
    private String primaryKey;
    private List<Column> columns;
}
