package ru.tyshchenko.vkr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tyshchenko.vkr.dto.controller.source.ControllerSource;
import ru.tyshchenko.vkr.dto.repository.source.RepositorySource;
import ru.tyshchenko.vkr.service.ControllerService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ControllerController {

    private final ControllerService service;

    @PostMapping("/controllers/save")
    public void buildControllers(@RequestBody List<ControllerSource> sources) {
        service.save(sources);
    }
}