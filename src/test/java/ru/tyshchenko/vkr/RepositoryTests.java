package ru.tyshchenko.vkr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyshchenko.vkr.dto.controller.source.ControllerMethodSource;
import ru.tyshchenko.vkr.dto.controller.source.ControllerSource;
import ru.tyshchenko.vkr.dto.entity.meta.EntityInfo;
import ru.tyshchenko.vkr.dto.entity.source.ColumnSource;
import ru.tyshchenko.vkr.dto.entity.source.Entity;
import ru.tyshchenko.vkr.dto.entity.source.ReferenceColumn;
import ru.tyshchenko.vkr.dto.entity.types.ColumnType;
import ru.tyshchenko.vkr.dto.entity.types.ReferenceType;
import ru.tyshchenko.vkr.dto.repository.enums.LinkCondition;
import ru.tyshchenko.vkr.dto.repository.enums.OperationCondition;
import ru.tyshchenko.vkr.dto.repository.enums.ReturnType;
import ru.tyshchenko.vkr.dto.repository.meta.RepositoryInfo;
import ru.tyshchenko.vkr.dto.repository.source.RepositoryMethodCondition;
import ru.tyshchenko.vkr.dto.repository.source.RepositoryMethodSource;
import ru.tyshchenko.vkr.dto.repository.source.RepositorySource;
import ru.tyshchenko.vkr.dto.service.meta.ServiceInfo;
import ru.tyshchenko.vkr.dto.service.source.ServiceMethodSource;
import ru.tyshchenko.vkr.dto.service.source.ServiceRepositoryMethodSource;
import ru.tyshchenko.vkr.dto.service.source.ServiceSource;
import ru.tyshchenko.vkr.factory.controller.SpringRestControllerFactory;
import ru.tyshchenko.vkr.factory.dto.entity.DtoMetaInfoFactory;
import ru.tyshchenko.vkr.factory.dto.request.RequestDtoFactory;
import ru.tyshchenko.vkr.factory.dto.response.ResponseDtoFactory;
import ru.tyshchenko.vkr.factory.entity.EntityInfoMetaInfoFactory;
import ru.tyshchenko.vkr.factory.repository.RepositoryMetaInfoFactory;
import ru.tyshchenko.vkr.factory.repository.java.JpaRepositoryFactory;
import ru.tyshchenko.vkr.factory.service.ServiceMetaInfoFactory;
import ru.tyshchenko.vkr.factory.service.ServiceSpringFactory;

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
                .repositoryMethods(List.of(repositoryMethodSource))
                .build();
        return new ArrayList<>(repositoryMetaInfoFactory.buildRepositoryInfo(List.of(source), entityInfos).values());
    }

    private Map<String, ServiceInfo> serviceInfos() {
        var repInfo = getRepInfo().get(0);
        ServiceSource serviceSource = new ServiceSource();
        serviceSource.setServiceName("Test");
        var methodSource = new ServiceMethodSource();
        methodSource.setName("testMethod");
        var repMethod = new ServiceRepositoryMethodSource();
        repMethod.setRepositoryName(repInfo.getName());
        repMethod.setMethodName(repInfo.getRepositoryMethodInfos()
                .values().stream()
                .findAny().get().getName());
        methodSource.setServiceRepositoryMethodSources(List.of(repMethod));
        serviceSource.setMethodSources(List.of(methodSource));
        return serviceMetaInfoFactory.buildServiceInfo(List.of(serviceSource), getRepInfo().stream()
                .collect(toMap(RepositoryInfo::getName, Function.identity())));
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
        controllerSource.setServiceName("TestService");
        var controllerMethodSource = new ControllerMethodSource();
        controllerMethodSource.setServiceMethod("testMethod");
        controllerMethodSource.setMapping("/hello/world");
        controllerMethodSource.setName("test");
        controllerSource.setControllerMethodSources(List.of(controllerMethodSource));
        System.out.println(springRestControllerFactory.buildControllers(List.of(controllerSource), serviceInfos()));
    }
}