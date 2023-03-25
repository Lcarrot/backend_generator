package ru.tyshchenko.vkr.repository.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodSource {

    public String returnType;
    public List<MethodCondition> condition;

}
