package ru.tyshchenko.vkr.engine.resolver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.factory.service.ServiceFactory;
import ru.tyshchenko.vkr.engine.api.models.controller.source.ControllerSource;
import ru.tyshchenko.vkr.engine.api.models.dto.entity.EntityDtoInfo;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.engine.api.models.service.source.ServiceSource;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ServiceResolver {

    private final List<ServiceFactory> serviceFactories;

    public Map<String, String> buildClasses(List<ServiceInfo> serviceSources, Map<String, EntityDtoInfo> entityDtoInfoMap,
                                            String language) {
        var factory = serviceFactories.stream()
                .filter(codeFactory -> codeFactory.language().equals(language))
                .findAny().orElseThrow();
        return factory.buildServices(serviceSources, entityDtoInfoMap);
    }
}
