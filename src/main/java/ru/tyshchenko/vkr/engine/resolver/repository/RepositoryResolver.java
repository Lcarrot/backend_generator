package ru.tyshchenko.vkr.engine.resolver.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.engine.api.factory.repository.RepositoryFactory;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RepositoryResolver {

    private final List<RepositoryFactory> factories;

    public Map<String, String> buildClasses(List<RepositoryInfo> entityInfos, String dialect) {
        RepositoryFactory factory = factories.stream()
                .filter(codeFactory -> codeFactory.language().equals(dialect))
                .findAny().orElseThrow();
        return factory.buildRepository(entityInfos);
    }
}
