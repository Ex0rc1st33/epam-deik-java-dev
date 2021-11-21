package com.epam.training.ticketservice.backend.screening.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ScreeningDTO {

    private final String movieTitle;
    private final String roomName;
    private final String startedAt;

    public ScreeningDTO(String movieTitle, String roomName, String startedAt) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.startedAt = startedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreeningDTO that = (ScreeningDTO) o;
        return Objects.equals(movieTitle, that.movieTitle) && Objects.equals(roomName, that.roomName) && Objects.equals(startedAt, that.startedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieTitle, roomName, startedAt);
    }

    @Override
    public String toString() {
        return "ScreeningDTO{" + "movieTitle='" + movieTitle + '\'' + ", roomName='" + roomName + '\'' + ", startedAt='" + startedAt + '\'' + '}';
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

        public ScreeningDTO build() {
            return new ScreeningDTO(movieTitle, roomName, startedAt);
        }

    }

}
