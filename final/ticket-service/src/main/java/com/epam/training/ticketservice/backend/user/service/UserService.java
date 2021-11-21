package com.epam.training.ticketservice.backend.user.service;

import com.epam.training.ticketservice.backend.user.model.UserDTO;

import java.util.Optional;

public interface UserService {

    String signInPrivileged(String username, String password);

    String signUp(String username, String password);

    String signIn(String username, String password);

    String signOut();

    Optional<UserDTO> getLoggedInUser();

}
