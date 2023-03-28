package ru.tyshchenko.vkr.dto.repository.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tyshchenko.vkr.dto.repository.enums.ReturnType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryMethodInfo {

    private String name;
    private ReturnType returnType;
    private List<ConditionInfo> conditionInfos;
}