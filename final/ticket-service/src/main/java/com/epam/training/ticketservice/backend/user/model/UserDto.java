package com.epam.training.ticketservice.backend.user.model;

import com.epam.training.ticketservice.backend.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDto {

    private final String username;
    private final User.Role role;

    public static Builder builder() {
        return new Builder();
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

        public UserDto build() {
            return new UserDto(username, role);
        }

    }

}
