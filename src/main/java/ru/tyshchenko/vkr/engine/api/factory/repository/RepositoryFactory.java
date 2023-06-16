package ru.tyshchenko.vkr.engine.api.factory.repository;

import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryInfo;

import java.util.Collection;
import java.util.Map;

public interface RepositoryFactory {

    Map<String, String> buildRepository(Collection<RepositoryInfo> repositoryInfos);

    String language();
}
