package ru.tyshchenko.vkr.dto.entity.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnSource {

    private String type;
    private String name;
    private Set<String> constraintTypes;
}
