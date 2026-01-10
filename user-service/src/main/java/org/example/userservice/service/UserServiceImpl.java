package org.example.userservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.userservice.dto.UserRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.dto.UserUpdateRequest;
import org.example.userservice.entity.User;
import org.example.userservice.event.UserEvent;
import org.example.userservice.exception.ResourceNotFoundException;
import org.example.userservice.exception.UserAlreadyExistsException;
import org.example.userservice.messaging.UserEventProducer;
import org.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventProducer userEventProducer;

    @Override
    @Transactional
    @CircuitBreaker(name = "userService", fallbackMethod = "createUserFallback")
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating user with email: {}", userRequest.getEmail());

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistsException(
                    String.format("User with email %s already exists", userRequest.getEmail())
            );
        }

        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        log.info("User created with id: {}", savedUser.getId());

        UserEvent event = new UserEvent(
                "USER_CREATED",
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getId(),
                LocalDateTime.now()
        );
        userEventProducer.sendUserEvent(event);

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "userService", fallbackMethod = "getUserByIdFallback")
    public UserResponse getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "userService", fallbackMethod = "getAllUsersFallback")
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "userService", fallbackMethod = "updateUserFallback")
    public UserResponse updateUser(Long id, UserUpdateRequest updateRequest) {
        log.info("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (updateRequest.getEmail() != null &&
                !updateRequest.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new UserAlreadyExistsException(
                    String.format("User with email %s already exists", updateRequest.getEmail())
            );
        }

        userMapper.updateEntity(updateRequest, user);
        User updatedUser = userRepository.save(user);
        log.info("User updated with id: {}", updatedUser.getId());

        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "userService", fallbackMethod = "deleteUserFallback")
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
        log.info("User deleted with id: {}", id);

        UserEvent event = new UserEvent(
                "USER_DELETED",
                user.getEmail(),
                user.getName(),
                user.getId(),
                LocalDateTime.now()
        );
        userEventProducer.sendUserEvent(event);
    }

    private UserResponse createUserFallback(UserRequest userRequest, Throwable t) {
        log.warn("Circuit Breaker triggered for user creation request - Name: {}, Email: {}, Age: {}. Message: {}",
                userRequest.getName(), userRequest.getEmail(), userRequest.getAge(), t.getMessage());
        System.out.println("Circuit Breaker triggered for user creation request with name " + userRequest.getName()
                + ", email " + userRequest.getEmail() + ", age " + userRequest.getAge());

        return UserResponse.builder()
                .id((long) -1)
                .name("Circuit breaker triggered")
                .age(-1)
                .email("-1")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private UserResponse getUserByIdFallback(Long id, Throwable t) {
        log.warn("Circuit Breaker triggered for request with user id: {}. Message: {}", id, t.getMessage());
        System.out.println("Circuit Breaker triggered for request with user id: " + id);
        return UserResponse.builder()
                .id(id)
                .name("Circuit breaker triggered")
                .age(-1)
                .email("-1")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<UserResponse> getAllUsersFallback(Throwable t) {
        log.warn("Circuit Breaker triggered for request for list of users. Message: {}", t.getMessage());
        System.out.println("Circuit Breaker triggered for request for list of users.");
        return Collections.emptyList();
    }

    private UserResponse updateUserFallback(Long id, UserUpdateRequest updateRequest, Throwable t) {
        log.warn("Circuit Breaker triggered for user update request with id: {}. Message: {}", id, t.getMessage());
        System.out.println("Circuit Breaker triggered for user update request with id: " + id);
        return UserResponse.builder()
                .id(id)
                .name("Circuit breaker triggered")
                .age(-1)
                .email("-1")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void deleteUserFallback(Long id, Throwable t) {
        log.warn("Circuit Breaker triggered for deletion request with id: {}. Message: {}", id, t.getMessage());
        System.out.println("Circuit Breaker triggered for deletion request with user id: " + id);
    }
}