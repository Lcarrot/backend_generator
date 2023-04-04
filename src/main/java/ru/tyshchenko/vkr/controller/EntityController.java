package ru.tyshchenko.vkr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tyshchenko.vkr.dto.entity.api.EntityApi;
import ru.tyshchenko.vkr.dto.entity.source.Entity;
import ru.tyshchenko.vkr.dto.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.service.EntityService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class EntityController {

    private final EntityService entityService;

    @PostMapping("/entities/save")
    public void buildEntities(List<Entity> entities, List<ReferenceColumn> referenceColumns) {
        entityService.save(entities, referenceColumns);
    }

    @GetMapping(value = "/entities/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EntityApi> getEntityApis() {
        return entityService.getApi();
    }
}