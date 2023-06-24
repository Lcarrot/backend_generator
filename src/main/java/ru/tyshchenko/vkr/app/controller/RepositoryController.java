package ru.tyshchenko.vkr.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.tyshchenko.vkr.engine.api.models.repository.api.RepositoryApi;
import ru.tyshchenko.vkr.engine.api.models.repository.source.RepositorySource;
import ru.tyshchenko.vkr.app.service.RepositoryService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class RepositoryController {

    private final RepositoryService repositoryService;

    @PostMapping("/repositories/save")
    public void buildRep(@RequestBody List<RepositorySource> sources) {
        repositoryService.save(sources);
    }

    @GetMapping(value = "/repositories/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepositoryApi> getApisRep() {
        return repositoryService.getApi();
    }
}