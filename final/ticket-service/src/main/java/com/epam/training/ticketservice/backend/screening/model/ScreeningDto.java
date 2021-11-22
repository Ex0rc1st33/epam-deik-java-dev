package com.epam.training.ticketservice.backend.screening.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ScreeningDto {

    private final String movieTitle;
    private final String roomName;
    private final String startedAt;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String movieTitle;
        private String roomName;
        private String startedAt;

        public Builder withMovieTitle(String movieTitle) {
            this.movieTitle = movieTitle;
            return this;
        }

        public Builder withRoomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder withStartedAt(String startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public ScreeningDto build() {
            return new ScreeningDto(movieTitle, roomName, startedAt);
        }

    }

}
