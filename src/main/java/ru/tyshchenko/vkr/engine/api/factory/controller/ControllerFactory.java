package ru.tyshchenko.vkr.engine.api.factory.controller;

import ru.tyshchenko.vkr.engine.api.models.controller.source.ControllerSource;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceInfo;

import java.util.List;
import java.util.Map;

public interface ControllerFactory {

    Map<String, String> buildControllers(List<ControllerSource> controllerSources,
                                                Map<String, ServiceInfo> serviceInfoMap);

    String language();
}
