package org.example.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.userservice.dto.UserRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.dto.UserUpdateRequest;
import org.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "API для управления пользователями")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя с указанными данными"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные входные данные"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Пользователь с таким email уже существует"
            )
    })
    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);

        EntityModel<UserResponse> resource = EntityModel.of(createdUser);
        resource.add(linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        resource.add(linkTo(methodOn(UserController.class).updateUser(createdUser.getId(), null)).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(createdUser.getId())).withRel("delete"));
        resource.add(Link.of("/swagger-ui.html", "api-docs"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает информацию о пользователе по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);

        EntityModel<UserResponse> resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        resource.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        resource.add(linkTo(UserController.class).slash("search").withRel("search"));
        resource.add(Link.of("/swagger-ui.html", "api-docs"));

        return ResponseEntity.ok(resource);
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей с поддержкой пагинации"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей получен",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();

        List<EntityModel<UserResponse>> userResources = users.stream()
                .map(user -> {
                    EntityModel<UserResponse> resource = EntityModel.of(user);
                    resource.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
                    resource.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel("update"));
                    resource.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"));
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserResponse>> collection = CollectionModel.of(userResources);
        collection.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        collection.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create-user"));
        collection.add(Link.of("/swagger-ui.html", "api-docs"));

        return ResponseEntity.ok(collection);
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет информацию о пользователе по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно обновлен",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверные входные данные"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Пользователь с таким email уже существует"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserResponse updatedUser = userService.updateUser(id, updateRequest);

        EntityModel<UserResponse> resource = EntityModel.of(updatedUser);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        resource.add(Link.of("/swagger-ui.html", "api-docs"));

        return ResponseEntity.ok(resource);
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}