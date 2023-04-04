package ru.tyshchenko.vkr.factory.repository;

import ru.tyshchenko.vkr.dto.repository.meta.RepositoryInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface RepositoryFactory {

    Map<String, String> buildRepository(Collection<RepositoryInfo> repositoryInfos);
}
