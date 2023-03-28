package ru.tyshchenko.vkr.dto.repository.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.dto.repository.enums.LinkCondition;
import ru.tyshchenko.vkr.dto.repository.enums.OperationCondition;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryMethodCondition {

    private String field;
    private String operationCondition;
    private String link;
}
