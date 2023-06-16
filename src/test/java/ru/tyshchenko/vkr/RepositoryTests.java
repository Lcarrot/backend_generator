package ru.tyshchenko.vkr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.vkr.engine.api.models.controller.source.ControllerMethodSource;
import ru.tyshchenko.vkr.engine.api.models.controller.source.ControllerSource;
import ru.tyshchenko.vkr.engine.api.models.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.engine.api.models.entity.source.ColumnSource;
import ru.tyshchenko.vkr.engine.api.models.entity.source.Entity;
import ru.tyshchenko.vkr.engine.api.models.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.engine.api.models.entity.types.ColumnType;
import ru.tyshchenko.vkr.engine.api.models.entity.types.ReferenceType;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.LinkCondition;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.OperationCondition;
import ru.tyshchenko.vkr.engine.api.models.repository.enums.ReturnType;
import ru.tyshchenko.vkr.engine.api.models.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.engine.api.models.repository.source.RepositoryMethodCondition;
import ru.tyshchenko.vkr.engine.api.models.repository.source.RepositoryMethodSource;
import ru.tyshchenko.vkr.engine.api.models.repository.source.RepositorySource;
import ru.tyshchenko.vkr.engine.api.models.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.engine.api.models.service.source.ServiceMethodSource;
import ru.tyshchenko.vkr.engine.api.models.service.source.ServiceRepositoryMethodSource;
import ru.tyshchenko.vkr.engine.api.models.service.source.ServiceSource;
import ru.tyshchenko.vkr.engine.impl.SupportedServiceLanguage;
import ru.tyshchenko.vkr.engine.impl.java.controller.SpringRestControllerFactory;
import ru.tyshchenko.vkr.engine.resolver.dto.entity.DtoMetaInfoFactory;
import ru.tyshchenko.vkr.engine.resolver.dto.request.RequestDtoFactory;
import ru.tyshchenko.vkr.engine.resolver.dto.response.ResponseDtoFactory;
import ru.tyshchenko.vkr.engine.resolver.entity.EntityInfoMetaInfoFactory;
import ru.tyshchenko.vkr.engine.resolver.repository.RepositoryMetaInfoFactory;
import ru.tyshchenko.vkr.engine.impl.java.repository.JpaRepositoryFactory;
import ru.tyshchenko.vkr.engine.resolver.service.ServiceMetaInfoFactory;
import ru.tyshchenko.vkr.engine.impl.java.service.ServiceSpringFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@SpringBootTest
class RepositoryTests {

    @Autowired
    private EntityInfoMetaInfoFactory entityInfoMetaInfoFactory;
    @Autowired
    private RepositoryMetaInfoFactory repositoryMetaInfoFactory;
    @Autowired
    private JpaRepositoryFactory repositoryFactory;
    @Autowired
    private ServiceSpringFactory serviceSpringFactory;
    @Autowired
    private DtoMetaInfoFactory dtoMetaInfoFactory;
    @Autowired
    private ServiceMetaInfoFactory serviceMetaInfoFactory;
    @Autowired
    private ResponseDtoFactory responseDtoFactory;
    @Autowired
    private RequestDtoFactory requestDtoFactory;
    @Autowired
    private SpringRestControllerFactory springRestControllerFactory;

    private List<Entity> getEntities() {
        ColumnSource column = ColumnSource.builder()
                .name("test_field")
                .type(ColumnType.TEXT.name())
                .isUnique(true)
                .build();
        ColumnSource secColumn = ColumnSource.builder()
                .name("date_field")
                .type(ColumnType.TIMESTAMP.name())
                .isNotNull(true)
                .build();
        Entity firstEntity = Entity.builder()
                .entityName("test_entity")
                .columns(List.of(column, secColumn))
                .primaryKey("pr_key")
                .build();
        Entity secondEntity = Entity.builder()
                .entityName("new_entity")
                .columns(List.of(column))
                .primaryKey("pr_key")
                .build();
        return List.of(firstEntity, secondEntity);
    }

    private List<ReferenceColumn> getReferenceColumns() {
        var entities = getEntities();
        return List.of(ReferenceColumn.builder()
                .entityTo(entities.get(0).getEntityName())
                .entityFrom(entities.get(1).getEntityName())
                .referenceType(ReferenceType.ONE_TO_MANY.name())
                .build());
    }

    private Map<String, EntityInfo> getEntityMetaInfo() {
        return entityInfoMetaInfoFactory.getMetaInfo(getEntities(), getReferenceColumns());
    }

    private List<RepositoryInfo> getRepInfo() {
        var entityInfos = getEntityMetaInfo();
        RepositoryMethodSource repositoryMethodSource = RepositoryMethodSource
                .builder()
                .returnType(ReturnType.ENTITY.name().toLowerCase())
                .conditions(List.of(
                        RepositoryMethodCondition.builder()
                                .operationCondition(OperationCondition.EQUAL_IGNORE_CASE.name())
                                .field("test_field")
                                .link(LinkCondition.AND.name())
                                .build(),
                        RepositoryMethodCondition.builder()
                                .operationCondition(OperationCondition.BETWEEN.name())
                                .field("date_field")
                                .build())
                ).build();

        RepositorySource source = RepositorySource.builder()
                .entityName(getEntities().get(0).getEntityName())
                .name("test_rep")
                .methods(List.of(repositoryMethodSource))
                .build();
        return new ArrayList<>(repositoryMetaInfoFactory.buildRepositoryInfo(List.of(source), entityInfos).values());
    }

    private Map<String, ServiceInfo> serviceInfos() {
        var repInfo = getRepInfo().get(0);
        ServiceSource serviceSource = new ServiceSource();
        serviceSource.setName("Test");
        var methodSource = new ServiceMethodSource();
        methodSource.setName("testMethod");
        var repMethod = new ServiceRepositoryMethodSource();
        repMethod.setRepositoryName(repInfo.getName());
        repMethod.setRepositoryMethod(repInfo.getRepositoryMethodInfos()
                .values().stream()
                .findAny().get().getName());
        methodSource.setRepositoryMethods(List.of(repMethod));
        serviceSource.setMethods(List.of(methodSource));
        return serviceMetaInfoFactory.buildServiceInfo(List.of(serviceSource), getRepInfo().stream()
                .collect(toMap(RepositoryInfo::getName, Function.identity())), SupportedServiceLanguage.JAVA_SPRING.name());
    }

    @Test
    void testRepBuild() {
        System.out.println(repositoryFactory.buildRepository(getRepInfo()));
    }

    @Test
    void testService() {
        var dtoInfo = dtoMetaInfoFactory.buildInfo(getRepInfo());
        System.out.println(serviceSpringFactory.buildServices(new ArrayList<>(serviceInfos().values()), dtoInfo));
    }

    @Test
    void buildResponseDto() {
        var serviceInfos = serviceInfos();
        var dtoInfo = dtoMetaInfoFactory.buildInfo(getRepInfo());
        System.out.println(responseDtoFactory.buildDtos(serviceInfos.values().stream()
                .map(ServiceInfo::getServiceMethodInfos).flatMap(Collection::stream).toList(), dtoInfo));
    }

    @Test
    void buildRequestDto() {
        System.out.println(requestDtoFactory.buildDtos(serviceInfos().values()
                .stream().map(ServiceInfo::getServiceMethodInfos).flatMap(Collection::stream).toList()));
    }

    @Test
    void testControllers() {
        var controllerSource = new ControllerSource();
        controllerSource.setName("Test");
        controllerSource.setService("TestService");
        var controllerMethodSource = new ControllerMethodSource();
        controllerMethodSource.setServiceMethod("testMethod");
        controllerMethodSource.setPath("/hello/world");
        controllerMethodSource.setName("test");
        controllerSource.setMethods(List.of(controllerMethodSource));
        System.out.println(springRestControllerFactory.buildControllers(List.of(controllerSource), serviceInfos()));
    }
}