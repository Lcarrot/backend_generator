package ru.tyshchenko.vkr.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.tyshchenko.vkr.engine.api.models.service.api.ServiceApi;
import ru.tyshchenko.vkr.engine.api.models.service.source.ServiceSource;
import ru.tyshchenko.vkr.app.service.ServiceService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping("/service/save")
    public void buildServ(@RequestBody List<ServiceSource> sources) {
        serviceService.save(sources);
    }

    @GetMapping(value = "/service/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceApi> getApisServ() {
        return serviceService.getApi();
    }
}