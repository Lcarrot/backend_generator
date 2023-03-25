package ru.tyshchenko.vkr.factory.repository;

import ru.tyshchenko.vkr.repository.meta.RepositoryInfo;

import java.util.List;

public interface RepositoryFactory {

    List<String> buildRepository(List<RepositoryInfo> repositoryInfos);
}
