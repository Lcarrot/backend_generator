package ru.tyshchenko.vkr.dto.entity.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.dto.entity.types.ReferenceType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceColumn {

    private String referenceType;
    private String entityTo;
    private String entityFrom;
}
