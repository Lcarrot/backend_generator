package ru.tyshchenko.vkr.engine.api.models.entity.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnSource {

    private String type;
    private String name;
    private Boolean isUnique;
    private Boolean isNotNull;
}
