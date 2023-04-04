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
public class ServiceController {

    private final RepositoryService repositoryService;

    @PostMapping("/service/save")
    public void buildServ(List<RepositorySource> sources) {
        repositoryService.save(sources);
    }

    @GetMapping(value = "/service/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepositoryApi> getApisServ() {
        return repositoryService.getApi();
    }
}