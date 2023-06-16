package ru.tyshchenko.vkr.engine.resolver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.factory.controller.ControllerFactory;
import ru.tyshchenko.vkr.engine.api.models.controller.source.ControllerSource;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceInfo;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ControllerResolver {

    private final List<ControllerFactory> controllerFactories;

    public Map<String, String> buildClasses(List<ControllerSource> controllerSources,
                               Map<String, ServiceInfo> serviceInfoMap, String language) {
        var factory = controllerFactories.stream()
                .filter(codeFactory -> codeFactory.language().equals(language))
                .findAny().orElseThrow();
        return factory.buildControllers(controllerSources, serviceInfoMap);
    }
}
