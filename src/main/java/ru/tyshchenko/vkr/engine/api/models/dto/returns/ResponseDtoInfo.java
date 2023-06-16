package ru.tyshchenko.vkr.engine.api.models.dto.returns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDtoInfo {

    private String name;
    @Builder.Default
    private List<PartDtoInfo> parts = new ArrayList<>();
}
