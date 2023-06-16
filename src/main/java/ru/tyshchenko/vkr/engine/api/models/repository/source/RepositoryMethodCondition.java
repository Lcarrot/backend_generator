package ru.tyshchenko.vkr.engine.api.models.repository.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryMethodCondition {

    private String field;
    private String operationCondition;
    private String link;
}
