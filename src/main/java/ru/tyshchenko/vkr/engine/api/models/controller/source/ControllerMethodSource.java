package ru.tyshchenko.vkr.engine.api.models.controller.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerMethodSource {

    private String name;
    private String path;
    private String serviceMethod;
}