package com.epam.training.ticketservice.backend.user.model;

import com.epam.training.ticketservice.backend.user.persistance.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class UserDTO {

    private final String username;
    private final User.Role role;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(username, userDTO.username) && role == userDTO.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role);
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username='" + username + '\'' + ", role=" + role + '}';
    }

    public static class Builder {

        private String username;
        private User.Role role;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withRole(User.Role role) {
            this.role = role;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(username, role);
        }

    }

}
