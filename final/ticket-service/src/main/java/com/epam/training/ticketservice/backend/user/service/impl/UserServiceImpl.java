package com.epam.training.ticketservice.backend.user.service.impl;

import com.epam.training.ticketservice.backend.user.model.UserDto;
import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import com.epam.training.ticketservice.backend.user.persistence.repository.UserRepository;
import com.epam.training.ticketservice.backend.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserDto loggedInUser = null;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String signInPrivileged(String username, String password) {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        Optional<User> userOptional = userRepository
                .findByUsernameAndPasswordAndRole(username, password, User.Role.ADMIN);
        if (userOptional.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        User user = userOptional.get();
        loggedInUser = UserDto.builder()
                .withUsername(user.getUsername())
                .withRole(user.getRole())
                .build();
        return "Signed in: " + loggedInUser + ", you have access to admin commands";
    }

    @Override
    public String signUp(String username, String password) {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        User user = new User(username, password, User.Role.USER);
        try {
            userRepository.save(user);
            return "Signed up new user: " + user;
        } catch (Exception e) {
            return "An unexpected exception occurred";
        }
    }

    @Override
    public String signIn(String username, String password) {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");
        Optional<User> userOptional = userRepository
                .findByUsernameAndPasswordAndRole(username, password, User.Role.USER);
        if (userOptional.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        User user = userOptional.get();
        loggedInUser = UserDto.builder()
                .withUsername(user.getUsername())
                .withRole(user.getRole())
                .build();
        return "Signed in: " + loggedInUser;
    }

    @Override
    public String signOut() {
        UserDto previousUser = loggedInUser;
        loggedInUser = null;
        if (previousUser == null) {
            return "You're currently signed out, you need to sign in first";
        }
        return "Signed out: " + previousUser;
    }

    @Override
    public Optional<UserDto> getLoggedInUser() {
        return Optional.ofNullable(loggedInUser);
    }

}
