package ru.tyshchenko.vkr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tyshchenko.vkr.dto.general.GeneralInfoDto;
import ru.tyshchenko.vkr.service.MainSpringMavenService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MainController {

    private final MainSpringMavenService service;

    @PostMapping(value = "/main/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createProject(@RequestBody GeneralInfoDto generalInfoDto) {
        service.createEmptyProject(generalInfoDto.getProjectPath());
        service.addDefaultInformation(generalInfoDto);
    }
}