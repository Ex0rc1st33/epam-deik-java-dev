package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.backend.user.model.UserDTO;
import com.epam.training.ticketservice.backend.user.persistance.entity.User;
import com.epam.training.ticketservice.backend.user.service.UserService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
public class UserCommand {

    private final UserService userService;

    public UserCommand(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod(key = "sign in privileged", value = "Login for admin users")
    public String signInPrivileged(String username, String password) {
        return userService.signInPrivileged(username, password);
    }

    @ShellMethod(key = "sign up", value = "Registration for non-admin users")
    public String signUp(String username, String password) {
        return userService.signUp(username, password);
    }

    @ShellMethod(key = "sign in", value = "Login for non-admin users")
    public String signIn(String username, String password) {
        return userService.signIn(username, password);
    }

    @ShellMethod(key = "sign out", value = "Logout from account")
    public String signOut() {
        return userService.signOut();
    }

    @ShellMethod(key = "describe account", value = "Get user information")
    public String describeAccount() {
        Optional<UserDTO> userDTO = userService.getLoggedInUser();
        if (userDTO.isEmpty()) {
            return "You are not signed in";
        }
        if (userDTO.get().getRole() == User.Role.ADMIN) {
            return "Signed in with privileged account '" + userDTO.get().getUsername() + "'";
        }
        return "Signed in with account " + userDTO.get().getUsername();
    }

}
