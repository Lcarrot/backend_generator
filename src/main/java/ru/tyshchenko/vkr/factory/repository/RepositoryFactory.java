package ru.tyshchenko.vkr.factory.repository;

import ru.tyshchenko.vkr.dto.repository.meta.RepositoryInfo;

import java.util.Collection;
import java.util.List;

public interface RepositoryFactory {

    List<String> buildRepository(Collection<RepositoryInfo> repositoryInfos);
}
