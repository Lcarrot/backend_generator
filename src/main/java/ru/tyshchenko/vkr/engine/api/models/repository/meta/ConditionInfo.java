package ru.tyshchenko.vkr.engine.api.models.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.OperationCondition;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.Column;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.LinkCondition;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConditionInfo {
    private Column column;
    private OperationCondition operationCondition;
    private LinkCondition linkCondition;
    private List<ArgumentInfo> argumentInfos;
}