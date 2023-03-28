package ru.tyshchenko.vkr.dto.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.dto.repository.enums.OperationCondition;
import ru.tyshchenko.vkr.dto.entity.meta.Column;
import ru.tyshchenko.vkr.dto.repository.enums.LinkCondition;

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