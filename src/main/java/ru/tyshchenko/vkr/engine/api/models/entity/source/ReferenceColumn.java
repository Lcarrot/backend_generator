package ru.tyshchenko.vkr.engine.api.models.entity.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceColumn {

    private String referenceType;
    private String entityTo;
    private String entityFrom;
}
