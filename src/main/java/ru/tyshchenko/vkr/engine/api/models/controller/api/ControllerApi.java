package ru.tyshchenko.vkr.engine.api.models.controller.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerApi {

    private List<ControllerMethodApi> controllerMethodApis;
}