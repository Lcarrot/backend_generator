package ru.tyshchenko.vkr.engine.resolver.entity.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.factory.entity.classes.EntityClassesCodeFactory;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EntityClassesResolver {

    private final List<EntityClassesCodeFactory> factories;

    public Map<String, String> buildEntityClasses(List<EntityInfo> entityInfos, String language) {
        return factories.stream()
                .filter(entityClassesCodeFactory -> entityClassesCodeFactory.language().equals(language))
                .findAny()
                .orElseThrow()
                .generate(entityInfos);
    }
}
