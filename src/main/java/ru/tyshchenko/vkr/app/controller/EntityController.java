package ru.tyshchenko.vkr.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.tyshchenko.vkr.engine.api.models.entity.api.EntityApi;
import ru.tyshchenko.vkr.engine.api.models.entity.source.EntityGenerateRequest;
import ru.tyshchenko.vkr.app.service.EntityService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class EntityController {

    private final EntityService entityService;

    @PostMapping("/entities/save")
    public void buildEntities(@RequestBody EntityGenerateRequest request) {
        entityService.save(request.getEntities(), request.getReferences());
    }

    @GetMapping(value = "/entities/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EntityApi> getEntityApis() {
        return entityService.getApi();
    }
}