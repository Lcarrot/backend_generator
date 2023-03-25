package ru.tyshchenko.vkr.repository.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.repository.enums.LinkCondition;
import ru.tyshchenko.vkr.repository.enums.OperationCondition;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodCondition {

    private String field;
    private OperationCondition operationCondition;
    private LinkCondition link;
}
