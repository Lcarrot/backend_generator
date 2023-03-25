package ru.tyshchenko.vkr.entity.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.entity.types.ReferenceType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceColumn {

    private ReferenceType referenceType;
    private String entityTo;
    private String entityFrom;
}
