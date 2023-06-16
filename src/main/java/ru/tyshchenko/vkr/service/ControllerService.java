package ru.tyshchenko.vkr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tyshchenko.vkr.context.SimpleBuilderContext;
import ru.tyshchenko.vkr.engine.api.models.controller.api.ControllerApi;
import ru.tyshchenko.vkr.engine.api.models.controller.source.ControllerSource;
import ru.tyshchenko.vkr.engine.impl.SupportedControllerLanguage;
import ru.tyshchenko.vkr.engine.resolver.controller.ControllerResolver;
import ru.tyshchenko.vkr.util.UploadUtils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ControllerService implements LayerService<ControllerSource, ControllerApi> {

    private final ControllerResolver resolver;
    private final SimpleBuilderContext context;

    @Override
    public void save(List<ControllerSource> source) {
        var values = resolver.buildClasses(source, context.getServiceInfoMap(),
                SupportedControllerLanguage.JAVA_SPRING.name());
        Path dirPath = context.getSourceCodePath().resolve("controller");
        UploadUtils.saveFile(dirPath, context.getPacket(), values);
    }

    @Override
    public List<ControllerApi> getApi() {
        return Collections.emptyList();
    }
}
