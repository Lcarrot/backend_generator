package ru.tyshchenko.vkr.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.entity.Column;
import ru.tyshchenko.vkr.repository.enums.LinkCondition;
import ru.tyshchenko.vkr.repository.enums.OperationCondition;

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