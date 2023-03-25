package ru.tyshchenko.vkr.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.repository.enums.ReturnType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo {

    private String name;
    private ReturnType returnType;
    private List<ConditionInfo> conditionInfos;
}