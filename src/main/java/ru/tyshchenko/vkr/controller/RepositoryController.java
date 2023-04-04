package ru.tyshchenko.vkr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tyshchenko.vkr.dto.repository.api.RepositoryApi;
import ru.tyshchenko.vkr.dto.repository.source.RepositorySource;
import ru.tyshchenko.vkr.service.RepositoryService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class RepositoryController {

    private final RepositoryService repositoryService;

    @PostMapping("/repositories/save")
    public void buildRep(List<RepositorySource> sources) {
        repositoryService.save(sources);
    }

    @GetMapping(value = "/repositories/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepositoryApi> getApisRep() {
        return repositoryService.getApi();
    }
}